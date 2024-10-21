package multithreading.trading_multithreading.dao;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadPayloadDAO {
    HikariDataSource dataSource = HikariCPConfig.getDataSource();

    public String readPayload(String tradeId) {
        String payloadString;
        String selectSQL = "SELECT payload FROM trade_payloads where trade_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
            selectStatement.setString(1, tradeId);
            ResultSet payload = selectStatement.executeQuery();
            if(payload.next()){
                payloadString = payload.getString("payload");
            } else {
                payloadString = "No data found for trade_id: "+tradeId;
            }
        } catch (SQLException e) {
            System.out.println("Error processing row: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return payloadString;
    }

    public boolean isValidCUSIPSymbol(String cusip) {
        String selectSQL = "SELECT symbol FROM SecuritiesReference where symbol = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSQL)){
            selectStatement.setString(1,cusip);
            ResultSet rs = selectStatement.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidCUSIPSymbolHibernate(String cusip) {  // -----Update
        String selectSQL = "SELECT symbol FROM SecuritiesReference where symbol = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSQL)){
            selectStatement.setString(1,cusip);
            ResultSet rs = selectStatement.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
