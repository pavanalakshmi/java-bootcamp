package multithreading.trading_multithreading.dao.hibernate;

import multithreading.trading_multithreading.dao.PositionsDAO;
import multithreading.trading_multithreading.entity.Positions;
import multithreading.trading_multithreading.model.Trade;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import java.sql.Connection;

public class HibernatePositionsDAO implements PositionsDAO {
    SessionFactory factory;
    private static HibernatePositionsDAO instance;

    public HibernatePositionsDAO() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Positions.class).buildSessionFactory();
    }

    public static synchronized HibernatePositionsDAO getInstance(){
        if (instance == null) {
            instance = new HibernatePositionsDAO();
        }
        return instance;
    }

    public void insertToPositions(Trade trade, Connection connection) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            Positions positions = new Positions();
            positions.setAccountNumber(trade.accountNumber());
            positions.setCusip(trade.cusip());
            positions.setPosition(trade.quantity());
            positions.setVersion(0);
            session.save(positions);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public int updatePositions(Trade trade, Connection connection, int version, int newQuantity) {
        Session session = null;
        Transaction transaction = null;
        int rowsAffected = 0;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            String hql = "UPDATE Positions p SET p.position = :newQuantity, p.version = :version WHERE p.accountNumber = :accountNumber AND p.cusip = :cusip";
            Query query = session.createQuery(hql);
            query.setParameter("newQuantity", newQuantity);
            query.setParameter("version", version);
            query.setParameter("accountNumber", trade.accountNumber());
            query.setParameter("cusip", trade.cusip());
            rowsAffected = query.executeUpdate();
            transaction.commit();

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
        return rowsAffected;
    }

    public void close() {
        factory.close();
    }
}

