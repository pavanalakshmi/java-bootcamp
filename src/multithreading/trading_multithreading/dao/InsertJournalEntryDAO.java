package multithreading.trading_multithreading.dao;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertJournalEntryDAO {
    HikariDataSource dataSource;
    InsertUpdatePayloadDAO insertPayloadDAO;

    public InsertJournalEntryDAO() {
        dataSource = HikariCPConfig.getDataSource();
        insertPayloadDAO = new InsertUpdatePayloadDAO();
    }

    public void insertToJournalEntry(String accountNumber, String cusip, String direction, int quantity, String tradeId) {
        String insertSQL = "INSERT INTO journal_entry VALUES (?,?,?,?,?)"; //total 9029
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)){
            insertStatement.setString(1, accountNumber);
            insertStatement.setString(2, cusip);
            insertStatement.setString(3, direction);
            insertStatement.setInt(4, quantity);
            insertStatement.setString(5, "true");
            int rowInserted = insertStatement.executeUpdate();
            if(rowInserted > 0){
                insertPayloadDAO.updatePayload(tradeId, "posted");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
