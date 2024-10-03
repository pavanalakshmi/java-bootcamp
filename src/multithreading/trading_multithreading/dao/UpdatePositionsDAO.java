package multithreading.trading_multithreading.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdatePositionsDAO {
    public int updatePositions(String accountNumber, int newQuantity, Connection connection, int version, String cusip) throws SQLException {
        String updateSQL = "UPDATE positions SET position = ?, version = version+1 WHERE account_number =? AND version = ? AND CUSIP = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
            updateStatement.setInt(1, newQuantity);
            updateStatement.setString(2, accountNumber);
            updateStatement.setInt(3, version);
            updateStatement.setString(4, cusip);
            int rowsAffected = updateStatement.executeUpdate();

            if(rowsAffected == 0){
                return 0;
            } else{
                return rowsAffected;
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}
