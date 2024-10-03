package multithreading.trading_multithreading.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertToPositionsDAO {
    public void insertToPositions(String accountNumber, String cusip, int quantity, Connection connection) {
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
}
