package multithreading.trading_multithreading.dao;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.entity.JournalEntry;
import multithreading.trading_multithreading.util.ApplicationConfigProperties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertJournalEntryDAO {
    HikariDataSource dataSource;
    InsertUpdatePayloadDAO insertPayloadDAO;
    ApplicationConfigProperties applicationConfigProperties;
    SessionFactory factory;

    public InsertJournalEntryDAO() {
        dataSource = HikariCPConfig.getDataSource();
        insertPayloadDAO = new InsertUpdatePayloadDAO();
        applicationConfigProperties = new ApplicationConfigProperties();
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(JournalEntry.class).buildSessionFactory();
    }

    public void insertToJournalEntry(String accountNumber, String cusip, String direction, int quantity, String tradeId) {
        String insertSQL = "INSERT INTO journal_entry VALUES (?,?,?,?,?)"; //total 9029
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
            insertStatement.setString(1, accountNumber);
            insertStatement.setString(2, cusip);
            insertStatement.setString(3, direction);
            insertStatement.setInt(4, quantity);
            insertStatement.setString(5, "true");
            int rowInserted = insertStatement.executeUpdate();
            if (rowInserted > 0) {
                insertPayloadDAO.updatePayload(tradeId, "posted");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertToJournalEntryHibernate(String accountNumber, String cusip, String direction, int quantity, String tradeId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();

            JournalEntry journalEntry = new JournalEntry();
            journalEntry.setAccountNumber(accountNumber);
            journalEntry.setCusip(cusip);
            journalEntry.setDirection(direction);
            journalEntry.setQuantity(quantity);

            session.save(journalEntry);
            transaction.commit();
            insertPayloadDAO.updatePayloadHibernate(tradeId, "posted");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of failure
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    public void close() {
        factory.close();
    }
}
