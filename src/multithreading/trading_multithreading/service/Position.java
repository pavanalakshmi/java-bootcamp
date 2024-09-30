package multithreading.trading_multithreading.service;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** positions(account_number VARCHAR(20), CUSIP VARCHAR(20), position_id int AUTO_INCREMENT PRIMARY KEY, position int, version int);
 1. select version from positions -> if -1, insert-> default version 0
 if not -1-> get quantity; update -> direction buy quantity+/ SELL quantity- ; version++
 if rows affected =0; rollback
 retry mechanism
 **/

public class Position {
    HikariDataSource dataSource = HikariCPConfig.getDataSource();
    public static final int MAX_RETRY_ATTEMPTS = 3; // Retry insertion up to 3 times

    void upsertPositions(String accountNumber, String CUSIP, String direction, int newQuantity) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            int retryCount = 0;
            while (retryCount < MAX_RETRY_ATTEMPTS) {
                try {
                    int version = getVersionFromPositions(accountNumber, connection, CUSIP);
                    int existingQuantity = getQuantityFromPositions(accountNumber, connection, CUSIP);
                    if (version == -1) {
                        insertToPositions(accountNumber, CUSIP, newQuantity, connection);
                    } else {
                        if (direction.equalsIgnoreCase("BUY")) {
                            newQuantity = existingQuantity + newQuantity;
                        } else if (direction.equalsIgnoreCase("SELL")) {
                            newQuantity = existingQuantity - newQuantity;
                        }
                        updatePositions(accountNumber, newQuantity, connection, version, CUSIP);
                    }
                    connection.commit();
                    break;
                } catch (Exception e) {
                    retryCount++;
                    if (retryCount >= MAX_RETRY_ATTEMPTS) {
                        throw new SQLException("Max retry attempts reached for account: " + accountNumber);
                    }
                    connection.rollback();
                    System.out.println("Retrying due to error in upsert positions: " + e.getMessage());
                }
            }
        }
    }

    private void updatePositions(String accountNumber, int newQuantity, Connection connection, int version, String cusip) throws SQLException {
        String updateSQL = "UPDATE positions SET position = ?, version = version+1 WHERE account_number =? AND version = ? AND CUSIP = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
            updateStatement.setInt(1, newQuantity);
            updateStatement.setString(2, accountNumber);
            updateStatement.setInt(3, version);
            updateStatement.setString(4, cusip);
            int rowsAffected = updateStatement.executeUpdate();

            if(rowsAffected == 0){
                connection.rollback();
                throw new SQLException("Optimistic locking failed: no rows updated for account: "+accountNumber); //9029
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    private int getQuantityFromPositions(String accountNumber, Connection connection, String CUSIP) {
        String selectSQL = "SELECT position FROM positions where account_number = ? AND CUSIP = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
            selectStatement.setString(1, accountNumber);
            selectStatement.setString(2, CUSIP);
            ResultSet rs = selectStatement.executeQuery();
            if(rs.next()){
                return  rs.getInt("position");
            }
            else{
                return -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertToPositions(String accountNumber, String cusip, int quantity, Connection connection) {
        String insertSQL = "INSERT INTO positions(account_number, CUSIP, position, version) VALUES (?,?,?,0)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL)){
            insertStatement.setString(1, accountNumber);
            insertStatement.setString(2, cusip);
            insertStatement.setInt(3, quantity);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getVersionFromPositions(String accountNumber, Connection connection, String CUSIP) {
        String selectSQL = "SELECT version FROM positions where account_number = ? and CUSIP = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
            selectStatement.setString(1, accountNumber);
            selectStatement.setString(2, CUSIP);
            ResultSet rs = selectStatement.executeQuery();
            if(rs.next()){
                return  rs.getInt("version");
            }
            else{
                return -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


