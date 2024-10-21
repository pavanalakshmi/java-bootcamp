package multithreading.trading_multithreading.dao.jdbc;

import multithreading.trading_multithreading.dao.PositionsDAO;
import multithreading.trading_multithreading.model.Trade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCPositionsDAO implements PositionsDAO {
    private static JDBCPositionsDAO instance;

    private JDBCPositionsDAO() {
    }

    public static synchronized JDBCPositionsDAO getInstance(){
        if (instance == null) {
            instance = new JDBCPositionsDAO();
        }
        return instance;
    }

    public void insertToPositions(Trade trade, Connection connection) {
        String insertSQL = "INSERT INTO positions(account_number, CUSIP, position, version) VALUES (?,?,?,0)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL)){
            insertStatement.setString(1, trade.accountNumber());
            insertStatement.setString(2, trade.cusip());
            insertStatement.setInt(3, trade.quantity());
            insertStatement.executeUpdate();
            // update journal entry add column as posted if inserted to positions table
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int updatePositions(Trade trade, Connection connection, int version, int newQuantity) throws SQLException {
        String updateSQL = "UPDATE positions SET position = ?, version = version+1 WHERE account_number =? AND version = ? AND CUSIP = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
            updateStatement.setInt(1, newQuantity);
            updateStatement.setString(2, trade.accountNumber());
            updateStatement.setInt(3, version);
            updateStatement.setString(4, trade.cusip());
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
