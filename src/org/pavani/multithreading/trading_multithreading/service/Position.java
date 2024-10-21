package org.pavani.multithreading.trading_multithreading.service;

import com.zaxxer.hikari.HikariDataSource;
import org.pavani.multithreading.trading_multithreading.config.HikariCPConfig;
import org.pavani.multithreading.trading_multithreading.dao.PositionsDAO;
import org.pavani.multithreading.trading_multithreading.dao.RetrievePositionsDataDAO;
import org.pavani.multithreading.trading_multithreading.factory.BeanFactory;
import org.pavani.multithreading.trading_multithreading.model.Trade;
import org.pavani.multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class Position {
    HikariDataSource dataSource;
    RetrievePositionsDataDAO retrievePositionsDataDAO;
    int maxRetryCount;
    private final Map<String, Integer> retryMap = new HashMap<>(); // for each account number
    private final LinkedBlockingQueue<String> deadLetterQueue = new LinkedBlockingQueue<>();
    private static PositionsDAO positionsDAO;
    private static ApplicationConfigProperties applicationConfigProperties;

    public Position() {
        dataSource = HikariCPConfig.getDataSource();
        retrievePositionsDataDAO = new RetrievePositionsDataDAO();
        applicationConfigProperties = ApplicationConfigProperties.getInstance();
        maxRetryCount = applicationConfigProperties.getMaxRetryAttempts();
        positionsDAO = BeanFactory.getPositionsDAO();
    }

    void upsertPositions(Trade trade) throws SQLException, InterruptedException {
        if (retryMap.getOrDefault(trade.accountNumber(), 0) >= maxRetryCount) {
            deadLetterQueue.offer(trade.accountNumber());
            System.out.println("Max retry attempts reached. Account moved to dead letter queue: " + trade.accountNumber());
            return;
        }
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            int retryCount = 0;
            while (retryCount < maxRetryCount) {
                try {
                    int version = retrievePositionsDataDAO.getVersionFromPositions(trade, connection);
                    int existingQuantity = retrievePositionsDataDAO.getQuantityFromPositions(trade, connection);
                    if (version == -1) {
                        positionsDAO.insertToPositions(trade, connection);
                    } else {
                        int newQuantity = getNewQuantity(trade.quantity(), existingQuantity, trade.direction());
                        int rowsAffected = positionsDAO.updatePositions(trade, connection, version, newQuantity);
                        if(rowsAffected == 0){
                            connection.rollback();
                            throw new SQLException("Optimistic locking failed: no rows updated for account: "+trade.accountNumber()); //9029
                        }
                    }
                    connection.commit();
                    retryMap.remove(trade.accountNumber());
                    break;
                } catch (Exception e) {
                    retryCount++;
                    retryMap.put(trade.accountNumber(), retryCount);
                    connection.rollback();
                    if (retryCount >= maxRetryCount) {
                        handleError(trade.accountNumber());
                        break;
                    }
                    System.out.println("Retrying due to error in upsert positions: " + e.getMessage());
                }
            }
        }
    }

    private int getNewQuantity(int newQuantity, int existingQuantity, String direction) {
        if (direction.equalsIgnoreCase("BUY")) {
            newQuantity = existingQuantity + newQuantity;
        } else if (direction.equalsIgnoreCase("SELL")) {
            newQuantity = existingQuantity - newQuantity;
        }
        return newQuantity;
    }

    private void handleError(String accountNumber) throws InterruptedException {
        int retryCount = retryMap.getOrDefault(accountNumber, 0);
        if (retryCount < maxRetryCount) {
            retryCount++;
            retryMap.put(accountNumber, retryCount);
            System.out.println("Retrying account: " + accountNumber + ", retry count: " + retryCount);
        } else {
            deadLetterQueue.put(accountNumber);
            System.out.println("Account " + accountNumber + " added to dead letter queue");
        }
    }
}