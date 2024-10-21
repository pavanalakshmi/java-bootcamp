package multithreading.trading_multithreading.dao.hibernate;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.dao.JournalEntryDAO;
import multithreading.trading_multithreading.dao.PayloadDAO;
import multithreading.trading_multithreading.entity.JournalEntry;
import multithreading.trading_multithreading.factory.BeanFactory;
import multithreading.trading_multithreading.model.Trade;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateJournalEntryDAO implements JournalEntryDAO {
    HikariDataSource dataSource;
    SessionFactory factory;
    private static HibernateJournalEntryDAO instance;
    private static PayloadDAO payloadDAO;

    private HibernateJournalEntryDAO() {
        dataSource = HikariCPConfig.getDataSource();
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(JournalEntry.class).buildSessionFactory();
        payloadDAO = BeanFactory.getPayloadDAO();
    }

    public static synchronized HibernateJournalEntryDAO getInstance(){
        if (instance == null) {
            instance = new HibernateJournalEntryDAO();
        }
        return instance;
    }

    public void insertToJournalEntry(Trade trade) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();

            JournalEntry journalEntry = new JournalEntry();
            journalEntry.setAccountNumber(trade.accountNumber());
            journalEntry.setCusip(trade.cusip());
            journalEntry.setDirection(trade.direction());
            journalEntry.setQuantity(trade.quantity());

            session.save(journalEntry);
            transaction.commit();
            payloadDAO.updatePayload(trade.tradeId(), "posted");

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
