package multithreading.trading_multithreading.dao;

import multithreading.trading_multithreading.entity.Positions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertToPositionsDAO {
    SessionFactory factory;

    public InsertToPositionsDAO() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Positions.class).buildSessionFactory();
    }

    public void insertToPositions(String accountNumber, String cusip, int quantity, Connection connection) {
        String insertSQL = "INSERT INTO positions(account_number, CUSIP, position, version) VALUES (?,?,?,0)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL)){
            insertStatement.setString(1, accountNumber);
            insertStatement.setString(2, cusip);
            insertStatement.setInt(3, quantity);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void insertToPositionsHibernate(String accountNumber, String cusip, int quantity) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();

            Positions positions = new Positions();
            positions.setAccountNumber(accountNumber);
            positions.setCusip(cusip);
            positions.setPosition(quantity);
            positions.setVersion(0);

            session.save(positions);
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
    }
    public void close() {
        factory.close();
    }
}
