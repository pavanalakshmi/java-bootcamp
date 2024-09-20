package jdbc.optimistic_locking.dao;

import jdbc.optimistic_locking.exception.OptimisticLockingException;
import jdbc.optimistic_locking.exception.SQLIntegrityConstraintViolationException;
import jdbc.optimistic_locking.model.CreditCardTransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Data Access Object
public class TransactionDAO {
    // Get the current version of the account for optimistic locking
    public static int getAccountVersion(Connection connection, String creditCardNumber) throws SQLException {
        String query = "SELECT version FROM accounts WHERE credit_card_number = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, creditCardNumber);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("version");
        } else {
            // Return -1 if no account exists for the given credit card number
            return -1;
        }
    }

    // Insert a new account for a credit card number
    public static void insertAccount(Connection connection, CreditCardTransaction creditCardTransaction) throws SQLException {
        connection.setAutoCommit(false);
        String insertQuery = "INSERT INTO accounts (credit_card_number, balance, version) VALUES (?, ?, 0)";
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)){
            stmt.setString(1, creditCardTransaction.getCreditCardNumber());
            stmt.setDouble(2, creditCardTransaction.getBalance() - creditCardTransaction.getAmount());
            int res = stmt.executeUpdate();
            if(res>0){
                System.out.println("Inserted new account for card: " + creditCardTransaction.getCreditCardNumber());
                connection.commit();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            if(!connection.getAutoCommit()){
                connection.rollback();
            }
            throw new SQLIntegrityConstraintViolationException("Duplicate insertions, retrying transaction...");
        }
//        connection.setAutoCommit(true);
    }

    // Update the account balance using optimistic locking
    public static void updateAccountBalance(Connection connection, CreditCardTransaction creditCardTransaction, int version) throws SQLException {
        connection.setAutoCommit(false);
        String updateQuery = "UPDATE accounts SET balance = ?, version = version + 1 WHERE credit_card_number = ? AND version = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)){
            stmt.setDouble(1, creditCardTransaction.getBalance() - creditCardTransaction.getAmount());
            stmt.setString(2, creditCardTransaction.getCreditCardNumber());
            stmt.setInt(3, version);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                connection.rollback(); // Optimistic locking failed, retry
                throw new OptimisticLockingException("Optimistic locking failed, retrying transaction...");
            } //        connection.commit();
        } catch (SQLException e) {
            if(!connection.getAutoCommit()){
                connection.rollback(); // Only rollback if autoCommit is false
                connection.setAutoCommit(true);
            }
            throw e;
//        } finally {
//            connection.setAutoCommit(true);  // Restore auto-commit to its original state
        }
    }
}
