package multithreading.trading_multithreading.service;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.dao.InsertToPositionsDAO;
import multithreading.trading_multithreading.dao.RetrievePositionsDataDAO;
import multithreading.trading_multithreading.dao.UpdatePositionsDAO;
import multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.sql.Connection;
import java.sql.SQLException;

/** positions(account_number VARCHAR(20), CUSIP VARCHAR(20), position_id int AUTO_INCREMENT PRIMARY KEY, position int, version int);
 1. select version from positions -> if -1, insert-> default version 0
 if not -1-> get quantity; update -> direction buy quantity+/ SELL quantity- ; version++
 if rows affected =0; rollback
 retry mechanism
 **/

public class Position {
    HikariDataSource dataSource;
    UpdatePositionsDAO updatePositionsDAO;
    InsertToPositionsDAO insertToPositionsDAO;
    RetrievePositionsDataDAO retrievePositionsDataDAO;
    ApplicationConfigProperties applicationConfigProperties;
    int maxRetryAttempts;

    public Position() {
        dataSource = HikariCPConfig.getDataSource();
        updatePositionsDAO = new UpdatePositionsDAO();
        insertToPositionsDAO = new InsertToPositionsDAO();
        retrievePositionsDataDAO = new RetrievePositionsDataDAO();

        applicationConfigProperties = new ApplicationConfigProperties();
        maxRetryAttempts = applicationConfigProperties.loadMaxRetryAttempts();
    }

    void upsertPositions(String accountNumber, String CUSIP, String direction, int newQuantity) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            int retryCount = 0;
            while (retryCount < maxRetryAttempts) {
                try {
                    int version = retrievePositionsDataDAO.getVersionFromPositions(accountNumber, connection, CUSIP);
                    int existingQuantity = retrievePositionsDataDAO.getQuantityFromPositions(accountNumber, connection, CUSIP);
                    if (version == -1) {
                        insertToPositionsDAO.insertToPositions(accountNumber, CUSIP, newQuantity, connection);
                    } else {
                        newQuantity = getNewQuantity(newQuantity, existingQuantity, direction);
                        int rowsAffected = updatePositionsDAO.updatePositions(accountNumber, newQuantity, connection, version, CUSIP);
                        if(rowsAffected == 0){
                            connection.rollback();
                            throw new SQLException("Optimistic locking failed: no rows updated for account: "+accountNumber); //9029
                        }
                    }
                    connection.commit();
                    break;
                } catch (Exception e) {
                    retryCount++;
                    if (retryCount >= maxRetryAttempts) {
                        throw new SQLException("Max retry attempts reached for account: " + accountNumber);
                    }
                    connection.rollback();
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
}
