package multithreading.trading_multithreading.dao;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.service.TradePayload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertPayloadDAO implements TradePayload {
    HikariDataSource dataSource;

    public InsertPayloadDAO() {
        dataSource = HikariCPConfig.getDataSource();
    }

    public void insertIntoPayload(String line){
        String insertSQL = "INSERT INTO trade_payloads(trade_id, status, payload) VALUES (?,?,?)";
        String[] data = line.split(",");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
            insertStatement.setString(1, data[0]);
            insertStatement.setString(3, line);
            String status = checkStatusOfPayload(line);
            insertStatement.setString(2, status);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error processing row: " + e.getMessage());
        }
    }

    public String checkStatusOfPayload(String line){
        return line.split(",").length ==7 ? "valid" : "invalid";
    }

}
