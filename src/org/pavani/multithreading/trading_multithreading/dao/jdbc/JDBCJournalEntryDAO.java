package org.pavani.multithreading.trading_multithreading.dao.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.pavani.multithreading.trading_multithreading.config.HikariCPConfig;
import org.pavani.multithreading.trading_multithreading.dao.JournalEntryDAO;
import org.pavani.multithreading.trading_multithreading.dao.PayloadDAO;
import org.pavani.multithreading.trading_multithreading.factory.BeanFactory;
import org.pavani.multithreading.trading_multithreading.model.Trade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCJournalEntryDAO implements JournalEntryDAO {
    HikariDataSource dataSource;
    private static JDBCJournalEntryDAO instance;
    private static PayloadDAO payloadDAO;

    private JDBCJournalEntryDAO() {
        dataSource = HikariCPConfig.getDataSource();
        payloadDAO = BeanFactory.getPayloadDAO();
    }

    public static synchronized JDBCJournalEntryDAO getInstance(){
        if (instance == null) {
            instance = new JDBCJournalEntryDAO();
        }
        return instance;
    }

    public void insertToJournalEntry(Trade trade) {
        String insertSQL = "INSERT INTO journal_entry VALUES (?,?,?,?,?)"; //total 9029
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
            insertStatement.setString(1, trade.accountNumber());
            insertStatement.setString(2, trade.cusip());
            insertStatement.setString(3, trade.direction());
            insertStatement.setInt(4, trade.quantity());
            insertStatement.setString(5, "true");
            int rowInserted = insertStatement.executeUpdate();
            if (rowInserted > 0) {
                payloadDAO.updatePayload(trade.tradeId(), "posted");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
