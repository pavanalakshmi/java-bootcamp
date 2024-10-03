package multithreading.trading_multithreading.dao;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertJournalEntryDAO {
    HikariDataSource dataSource = HikariCPConfig.getDataSource();
    public void insertToJournalEntry(String account_number, String CUSIP, String direction, int quantity) {
        String insertSQL = "INSERT INTO journal_entry VALUES (?,?,?,?,?)"; //total 9029
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)){
            insertStatement.setString(1, account_number);
            insertStatement.setString(2, CUSIP);
            insertStatement.setString(3, direction);
            insertStatement.setInt(4, quantity);
            insertStatement.setString(5, "true");
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
