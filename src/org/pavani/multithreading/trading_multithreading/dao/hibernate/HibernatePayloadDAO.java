package org.pavani.multithreading.trading_multithreading.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.pavani.multithreading.trading_multithreading.config.HibernateConfig;
import org.pavani.multithreading.trading_multithreading.dao.PayloadDAO;
import org.pavani.multithreading.trading_multithreading.dao.ReadPayloadDAO;
import org.pavani.multithreading.trading_multithreading.dao.RetrieveJournalEntryDAO;
import org.pavani.multithreading.trading_multithreading.entity.TradePayloads;

import javax.persistence.Query;
import java.util.logging.Logger;

public class HibernatePayloadDAO implements PayloadDAO {
    ReadPayloadDAO readPayloadDAO;
    RetrieveJournalEntryDAO retrieveJournalEntryDAO;
    SessionFactory factory;
    private static HibernatePayloadDAO instance;
    Logger logger = Logger.getLogger(HibernatePayloadDAO.class.getName());

    public HibernatePayloadDAO() {
        readPayloadDAO = new ReadPayloadDAO();
        retrieveJournalEntryDAO = new RetrieveJournalEntryDAO();
        factory = HibernateConfig.getSessionFactory();
    }

    public static synchronized HibernatePayloadDAO getInstance(){
        if (instance == null) {
            instance = new HibernatePayloadDAO();
        }
        return instance;
    }

    public void insertIntoPayload(String line){
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
        HibernateConfig.shutdown();
    }

    public void updatePayload(String tradeId, String newStatus){
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

