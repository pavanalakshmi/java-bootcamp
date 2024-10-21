package org.pavani.multithreading.trading_multithreading.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.pavani.multithreading.trading_multithreading.config.HikariCPConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RetrieveJournalEntryDAO {
    HikariDataSource dataSource;

    public RetrieveJournalEntryDAO() {
        dataSource = HikariCPConfig.getDataSource();
    }

    public boolean isJournalEntryExist(String accountNumber, String cusip){
        String querySQL = "SELECT quantity FROM journal_entry WHERE account_number = ? AND CUSIP = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(querySQL)){
            insertStatement.setString(1, accountNumber);
            insertStatement.setString(2, cusip);
            ResultSet resultSet = insertStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean isJournalEntryExistHibernate(String accountNumber, String cusip){ // ----Update
        String querySQL = "SELECT quantity FROM journal_entry WHERE account_number = ? AND CUSIP = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(querySQL)){
            insertStatement.setString(1, accountNumber);
            insertStatement.setString(2, cusip);
            ResultSet resultSet = insertStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
