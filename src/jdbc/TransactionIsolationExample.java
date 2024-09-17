package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionIsolationExample {
    public static void main(String[] args) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();

        try {
            // Set the transaction isolation level
            connection.setTransactionIsolation(connection.TRANSACTION_READ_COMMITTED);
            // Begin transaction
            connection.setAutoCommit(false);
            try {
                PreparedStatement stmt = connection.prepareStatement("UPDATE bank_account SET balance = balance - ? WHERE account_id = ?");
                stmt.setDouble(1, 100.0);
                stmt.setInt(2, 1);
                stmt.executeUpdate();
                // Commit the transaction
                connection.commit();
            } catch (SQLException e) {
                // Rollback in case of an error
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
