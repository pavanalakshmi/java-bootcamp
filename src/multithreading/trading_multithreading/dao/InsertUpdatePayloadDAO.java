package multithreading.trading_multithreading.dao;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.entity.TradePayloads;
import multithreading.trading_multithreading.service.TradePayload;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InsertUpdatePayloadDAO implements TradePayload {
    HikariDataSource dataSource;
    ReadPayloadDAO readPayloadDAO;
    RetrieveJournalEntryDAO retrieveJournalEntryDAO;
    String insertSQL = "INSERT INTO trade_payloads(trade_id, validity_status, payload, lookup_status, je_status) VALUES (?,?,?,?,?)";
    SessionFactory factory;

    public InsertUpdatePayloadDAO() {
        readPayloadDAO = new ReadPayloadDAO();
        dataSource = HikariCPConfig.getDataSource();
        retrieveJournalEntryDAO = new RetrieveJournalEntryDAO();
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(TradePayload.class).buildSessionFactory();
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

    public void insertIntoPayloadHibernate(String line){
        Session session = null;
        Transaction transaction = null;
        String[] data = line.split(",");
        String status = checkValidPayloadStatus(data) ? "valid" : "invalid";
        boolean validCusip = readPayloadDAO.isValidCUSIPSymbolHibernate(data[3]);
        String lookUpStatus = validCusip ? "pass" : "fail";
        boolean journalEntryStatus = retrieveJournalEntryDAO.isJournalEntryExistHibernate(data[2], data[3]);
        String jeStatus = journalEntryStatus ? "posted" : "not_posted";

        try {
            session = factory.openSession();
            transaction = session.beginTransaction();

            TradePayloads tradePayloads = new TradePayloads();
            tradePayloads.setTradeId(data[0]);
            tradePayloads.setValidityStatus(status);
            tradePayloads.setPayload(line);
            tradePayloads.setLookupStatus(lookUpStatus);
            tradePayloads.setJeStatus(jeStatus);

            session.save(tradePayloads);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of failure
            }
            System.out.println("Error while inserting row: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    public void close() {
        factory.close();
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

    public void updatePayloadHibernate(String tradeId, String newStatus){
        Session session = null;
        Transaction transaction = null;

        try {
            session = factory.openSession();
            transaction = session.beginTransaction();

            String hql = "UPDATE TradePayloads SET jeStatus = :newStatus WHERE tradeId = :tradeId";
            Query query = session.createQuery(hql);
            query.setParameter("newStatus", newStatus);
            query.setParameter("tradeId", tradeId);
            int result = query.executeUpdate();
            transaction.commit();
            System.out.println("Rows affected: " + result);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of failure
            }
            System.out.println("Error updating row: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }

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
