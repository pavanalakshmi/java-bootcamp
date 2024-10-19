package multithreading.trading_multithreading.service;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.dao.InsertToPositionsDAO;
import multithreading.trading_multithreading.dao.RetrievePositionsDataDAO;
import multithreading.trading_multithreading.dao.UpdatePositionsDAO;
import multithreading.trading_multithreading.util.ApplicationConfigProperties;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class Position {
    HikariDataSource dataSource;
    UpdatePositionsDAO updatePositionsDAO;
    InsertToPositionsDAO insertToPositionsDAO;
    RetrievePositionsDataDAO retrievePositionsDataDAO;
    ApplicationConfigProperties applicationConfigProperties;
    int maxRetryCount;
    private final Map<String, Integer> retryMap = new HashMap<>(); // for each account number
    private final LinkedBlockingQueue<String> deadLetterQueue = new LinkedBlockingQueue<>();

    public Position() {
        dataSource = HikariCPConfig.getDataSource();
        updatePositionsDAO = new UpdatePositionsDAO();
        insertToPositionsDAO = new InsertToPositionsDAO();
        retrievePositionsDataDAO = new RetrievePositionsDataDAO();
        applicationConfigProperties = new ApplicationConfigProperties();
        maxRetryCount = applicationConfigProperties.loadMaxRetryAttempts();
    }

    void upsertPositions(String accountNumber, String cusip, String direction, int newQuantity) throws SQLException, InterruptedException {
        if (retryMap.getOrDefault(accountNumber, 0) >= maxRetryCount) {
            deadLetterQueue.offer(accountNumber);
            System.out.println("Max retry attempts reached. Account moved to dead letter queue: " + accountNumber);
            return;
        }
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            int retryCount = 0;
            while (retryCount < maxRetryCount) {
                try {
                    int version = retrievePositionsDataDAO.getVersionFromPositions(accountNumber, connection, cusip);
                    int existingQuantity = retrievePositionsDataDAO.getQuantityFromPositions(accountNumber, connection, cusip);
                    if (version == -1) {
                        if(applicationConfigProperties.useHibernate()){
                            insertToPositionsDAO.insertToPositionsHibernate(accountNumber, cusip, newQuantity);
                        } else{
                            insertToPositionsDAO.insertToPositions(accountNumber, cusip, newQuantity, connection);
                        }
                    } else {
                        newQuantity = getNewQuantity(newQuantity, existingQuantity, direction);
                        int rowsAffected = updatePositionsDAO.updatePositions(accountNumber, newQuantity, connection, version, cusip);
                        if(rowsAffected == 0){
                            connection.rollback();
                            throw new SQLException("Optimistic locking failed: no rows updated for account: "+accountNumber); //9029
                        }
                    }
                    connection.commit();
                    retryMap.remove(accountNumber);
                    break;
                } catch (Exception e) {
                    retryCount++;
                    retryMap.put(accountNumber, retryCount);
                    connection.rollback();
                    if (retryCount >= maxRetryCount) {
                        handleError(accountNumber);
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