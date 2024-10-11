package multithreading.trading_multithreading.dao;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.service.TradePayload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InsertUpdatePayloadDAO implements TradePayload {
    HikariDataSource dataSource;
    ReadPayloadDAO readPayloadDAO;
    RetrieveJournalEntryDAO retrieveJournalEntryDAO;
    String insertSQL = "INSERT INTO trade_payloads(trade_id, validity_status, payload, lookup_status, je_status) VALUES (?,?,?,?,?)";

    public InsertUpdatePayloadDAO() {
        readPayloadDAO = new ReadPayloadDAO();
        dataSource = HikariCPConfig.getDataSource();
        retrieveJournalEntryDAO = new RetrieveJournalEntryDAO();
    }

    public void insertIntoPayload(String line){
        String[] data = line.split(",");
        String status = checkValidPayloadStatus(data) ? "valid" : "invalid";
        boolean validCusip = readPayloadDAO.isValidCUSIPSymbol(data[3]);
        String lookUpStatus = validCusip ? "pass" : "fail";
        boolean journalEntryStatus = retrieveJournalEntryDAO.isJournalEntryExist(data[2], data[3]);
        String jeStatus = journalEntryStatus ? "posted" : "not_posted";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
            insertStatement.setString(1, data[0]);
            insertStatement.setString(2, status);
            insertStatement.setString(3, line);
            insertStatement.setString(4, lookUpStatus);
            insertStatement.setString(5, jeStatus);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error processing row: " + e.getMessage());
        }
    }

    public void insertIntoPayloadBatch(List<String> lines) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
            for (String line : lines) {
                String[] data = line.split(",");
                String status = checkValidPayloadStatus(data) ? "valid" : "invalid";
                boolean validCusip = readPayloadDAO.isValidCUSIPSymbol(data[3]);
                String lookUpStatus = validCusip ? "pass" : "fail";
                boolean journalEntryStatus = retrieveJournalEntryDAO.isJournalEntryExist(data[2], data[3]);
                String jeStatus = journalEntryStatus ? "posted" : "not_posted";

                insertStatement.setString(1, data[0]);
                insertStatement.setString(2, status);
                insertStatement.setString(3, line);
                insertStatement.setString(4, lookUpStatus);
                insertStatement.setString(5, jeStatus);
                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error processing batch: " + e.getMessage());
        }
    }

    public void updatePayload(String tradeId, String newStatus){
        String updateSQL = "UPDATE trade_payloads SET je_status = ? WHERE trade_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
            updateStatement.setString(1, newStatus);
            updateStatement.setString(2, tradeId);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating row: " + e.getMessage());
        }
    }

    private boolean checkValidPayloadStatus(String[] data) {
        if (data.length != 7) {
            return false;
        }
        for(String s:data){
            if(s == null || s.trim().isEmpty()){
                return false;
            }
        }
        return true;
    }
}
