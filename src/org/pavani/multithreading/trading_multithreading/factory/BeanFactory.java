package org.pavani.multithreading.trading_multithreading.factory;

import org.pavani.multithreading.trading_multithreading.dao.JournalEntryDAO;
import org.pavani.multithreading.trading_multithreading.dao.PayloadDAO;
import org.pavani.multithreading.trading_multithreading.dao.PositionsDAO;
import org.pavani.multithreading.trading_multithreading.dao.hibernate.HibernateJournalEntryDAO;
import org.pavani.multithreading.trading_multithreading.dao.hibernate.HibernatePayloadDAO;
import org.pavani.multithreading.trading_multithreading.dao.hibernate.HibernatePositionsDAO;
import org.pavani.multithreading.trading_multithreading.dao.jdbc.JDBCJournalEntryDAO;
import org.pavani.multithreading.trading_multithreading.dao.jdbc.JDBCPayloadDAO;
import org.pavani.multithreading.trading_multithreading.dao.jdbc.JDBCPositionsDAO;
import org.pavani.multithreading.trading_multithreading.exception.InvalidPersistenceTechnologyException;
import org.pavani.multithreading.trading_multithreading.util.ApplicationConfigProperties;

public class BeanFactory {
    private static ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getInstance();
    private BeanFactory() {
    }
    private static final String HIBERNATE_PERSISTENCE_TECHNOLOGY = "hibernate";
    private static final String JDBC_PERSISTENCE_TECHNOLOGY = "jdbc";
    private static final String INVALID_PERSISTENCE_TECHNOLOGY = "Invalid persistence technology";

    public static JournalEntryDAO getJournalEntryDAO(){
        if(HIBERNATE_PERSISTENCE_TECHNOLOGY.equals(applicationConfigProperties.getPersistenceTechnology())){
            return HibernateJournalEntryDAO.getInstance();
        } else if (JDBC_PERSISTENCE_TECHNOLOGY.equals(applicationConfigProperties.getPersistenceTechnology())){
            return JDBCJournalEntryDAO.getInstance();
        } else{
            throw new InvalidPersistenceTechnologyException(INVALID_PERSISTENCE_TECHNOLOGY);
        }
    }

    public static PayloadDAO getPayloadDAO(){
        if(HIBERNATE_PERSISTENCE_TECHNOLOGY.equals(applicationConfigProperties.getPersistenceTechnology())){
            return HibernatePayloadDAO.getInstance();
        } else if (JDBC_PERSISTENCE_TECHNOLOGY.equals(applicationConfigProperties.getPersistenceTechnology())){
            return JDBCPayloadDAO.getInstance();
        } else{
            throw new InvalidPersistenceTechnologyException(INVALID_PERSISTENCE_TECHNOLOGY);
        }
    }

    public static PositionsDAO getPositionsDAO(){
        if(HIBERNATE_PERSISTENCE_TECHNOLOGY.equals(applicationConfigProperties.getPersistenceTechnology())){
            return HibernatePositionsDAO.getInstance();
        } else if (JDBC_PERSISTENCE_TECHNOLOGY.equals(applicationConfigProperties.getPersistenceTechnology())){
            return JDBCPositionsDAO.getInstance();
        } else{
            throw new InvalidPersistenceTechnologyException(INVALID_PERSISTENCE_TECHNOLOGY);
        }
    }
}
