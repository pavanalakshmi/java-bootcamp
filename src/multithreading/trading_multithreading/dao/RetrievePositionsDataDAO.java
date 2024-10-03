package multithreading.trading_multithreading.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RetrievePositionsDataDAO {
    public int getQuantityFromPositions(String accountNumber, Connection connection, String CUSIP) {
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

    public int getVersionFromPositions(String accountNumber, Connection connection, String CUSIP) {
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
