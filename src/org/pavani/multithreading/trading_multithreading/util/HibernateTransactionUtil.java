package org.pavani.multithreading.trading_multithreading.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.pavani.multithreading.trading_multithreading.entity.JournalEntry;
import org.pavani.multithreading.trading_multithreading.entity.Positions;
import org.pavani.multithreading.trading_multithreading.entity.TradePayloads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateTransactionUtil implements TransactionUtil{
    private static SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(HibernateTransactionUtil.class);
    private static final ThreadLocal<Session> threadLocalSession = new ThreadLocal<>();
    private static HibernateTransactionUtil instance;

    private HibernateTransactionUtil() {
    }

    public static synchronized HibernateTransactionUtil getInstance() {
        if (instance == null) {
            instance = new HibernateTransactionUtil();
        }
        return instance;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(JournalEntry.class)
                    .addAnnotatedClass(Positions.class)
                    .addAnnotatedClass(TradePayloads.class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }

    public static Session getConnection(){
        Session session = threadLocalSession.get();
        if (session == null) {
            session = getSessionFactory().openSession();
            threadLocalSession.set(session);
            String createSession = "New session created for thread: " + Thread.currentThread().getName();
            logger.info(createSession);
        } else {
            String existingSession = "Reusing existing session for thread: " + Thread.currentThread().getName();
            logger.info(existingSession);
        }
        return session;
    }

    public void startTransaction() {
        getConnection().beginTransaction();
    }

    private void closeConnection() {
        Session session = threadLocalSession.get();
        if (session != null) {
            session.close();
            threadLocalSession.remove();
        }
    }

    public void commitTransaction() {
        getConnection().getTransaction().commit();
        closeConnection();
    }

    public void rollbackTransaction() {
        getConnection().getTransaction().rollback();
        closeConnection();
    }

}
