package multithreading.trading_multithreading.service;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.dao.InsertJournalEntryDAO;
import multithreading.trading_multithreading.dao.ReadPayloadDAO;
import multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TradeProcessorService implements TradeProcessor {
    private final ExecutorService executor;
    HikariDataSource dataSource;
    Position position;
    ReadPayloadDAO readPayloadDAO;
    InsertJournalEntryDAO insertJournalEntryDAO;
    ApplicationConfigProperties applicationConfigProperties = new ApplicationConfigProperties();
    Map<String, LinkedBlockingQueue<String>> map;
    int queueCount;
    int maxRetryCount;

    public TradeProcessorService(Map<String, LinkedBlockingQueue<String>> queuesMap) {
        dataSource = HikariCPConfig.getDataSource();
        position = new Position();
        readPayloadDAO = new ReadPayloadDAO();
        insertJournalEntryDAO = new InsertJournalEntryDAO();
        queueCount = applicationConfigProperties.loadTradeProcessorQueueCount();
        map = queuesMap;
        executor = Executors.newFixedThreadPool(applicationConfigProperties.loadTradeProcessorThreadPoolSize());
        maxRetryCount = applicationConfigProperties.loadMaxRetryAttempts();
    }

    public void processTrade() {
        for (int i = 0; i < queueCount; i++) {
            executor.submit(() -> {
                try {
                    processAllQueues();
                } catch (InterruptedException | SQLException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Error while processing queue: "+e.getMessage());
                }
            });
        }
//        executor.shutdown();
//        try {
//            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
//                executor.shutdownNow();  // Forcibly terminate if needed
//            }
//        } catch (InterruptedException e) {
//            executor.shutdownNow();
////            Thread.currentThread().interrupt();
//        }
    }

    private void processAllQueues() throws InterruptedException, SQLException {
        while (true) {
            boolean isQueueEmpty = false;
            for (Map.Entry<String, LinkedBlockingQueue<String>> entry : map.entrySet()) {
                processQueue(entry.getValue());
                if (!processQueue(entry.getValue())) {
                    return;
                } else {
                    isQueueEmpty = true;
                }
            }
            if (!isQueueEmpty) {
                Thread.sleep(10);
            }
        }
    }

    private boolean processQueue(LinkedBlockingQueue<String> queue) throws InterruptedException, SQLException {
        String criteria = applicationConfigProperties.loadCriteriaTradeOrAccNo();
        boolean isProcessed = false;
        if(criteria.equals("tradeId")){
            isProcessed = processTradeQueue(queue);
        } else if (criteria.equals("accountNumber")) {
//            isProcessed = processAccountNumberQueue(queue);
            System.out.println("Logic not yet implemented");
        }
        return isProcessed;
    }

    /**
    private boolean processAccountNumberQueue(LinkedBlockingQueue<String> queue) throws InterruptedException {
        String tradeId;
        String accountNumber;
        String cusip;
        String direction;
        int quantity;
        while ((accountNumber = queue.poll(20, TimeUnit.SECONDS)) != null) {
            if (accountNumber.equals("END")) {
                return false;
            }
            String payload = readPayloadDAO.readPayload(tradeId);
            String[] payloadData = payload.split(",");
            accountNumber = payloadData[2];
            cusip = payloadData[3];
            direction = payloadData[4];
            quantity = Integer.parseInt(payloadData[5]);
            if (readPayloadDAO.isValidCUSIPSymbol(cusip)) {
                try{
                    insertJournalEntryDAO.insertToJournalEntry(accountNumber, cusip, direction, quantity, tradeId);
                    position.upsertPositions(accountNumber, cusip, direction, quantity);
                    retryMap.remove(tradeId); //removes tradeId from retryMap on success.
                } catch (SQLException e){
                    handleError(tradeId, queue);
                }
            }
        }
        return true;
    }
     **/

    private boolean processTradeQueue(LinkedBlockingQueue<String> queue) throws InterruptedException, SQLException {
        String tradeId;
        String accountNumber;
        String cusip;
        String direction;
        int quantity;
        while ((tradeId = queue.poll(20, TimeUnit.SECONDS)) != null) {
            if (tradeId.equals("END")) {
                return false;
            }
            String payload = readPayloadDAO.readPayload(tradeId);
            String[] payloadData = payload.split(",");
            accountNumber = payloadData[2];
            cusip = payloadData[3];
            direction = payloadData[4];
            quantity = Integer.parseInt(payloadData[5]);
            if (readPayloadDAO.isValidCUSIPSymbol(cusip)) {
                    insertJournalEntryDAO.insertToJournalEntry(accountNumber, cusip, direction, quantity, tradeId);
                    position.upsertPositions(accountNumber, cusip, direction, quantity);
            }
        }
        return true;
    }
}