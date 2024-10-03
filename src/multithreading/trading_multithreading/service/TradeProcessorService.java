package multithreading.trading_multithreading.service;

// reads queues trade_id and run threads in parallel
// read payload from trade_payloads table
// looks up symbol in securities table; if available -> add
// acc no, CUSIP, direction, quantity, posted_status

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.dao.InsertJournalEntryDAO;
import multithreading.trading_multithreading.dao.ReadPayloadDAO;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TradeProcessorService implements TradeProcessor {
    private ExecutorService executor;
    HikariDataSource dataSource;
    Position position;
    ReadPayloadDAO readPayloadDAO;
    InsertJournalEntryDAO insertJournalEntryDAO;

    public TradeProcessorService() {
        executor = Executors.newFixedThreadPool(3);
        dataSource = HikariCPConfig.getDataSource();
        position = new Position();
        readPayloadDAO = new ReadPayloadDAO();
        insertJournalEntryDAO = new InsertJournalEntryDAO();
    }

    public void processTrade(Map<String , LinkedBlockingQueue<String>> tradeDistributionQueuesMap){
        try {
            for(Map.Entry<String , LinkedBlockingQueue<String>> entry: tradeDistributionQueuesMap.entrySet()){
                LinkedBlockingQueue<String> queue = entry.getValue();
                executor.submit(() -> {
                    try {
                        processQueue(queue);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();  // Forcibly terminate if needed
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void processQueue(LinkedBlockingQueue<String> queue) throws SQLException {
//        for(String tradeId: queue){ readPayload(tradeId); }
        String tradeId;
        String account_number = "";
        String CUSIP = "";
        String direction = "";
        int quantity = 0;
        while ((tradeId = queue.poll()) != null) {
            String payload = readPayloadDAO.readPayload(tradeId);
            String[] payloadData = payload.split(",");
            account_number = payloadData[2];
            CUSIP = payloadData[3];
            direction = payloadData[4];
            quantity = Integer.parseInt(payloadData[5]);
//          account_number = payload.getString("account_number");

            if(readPayloadDAO.isValidCUSIPSymbol(CUSIP)){
                insertJournalEntryDAO.insertToJournalEntry(account_number, CUSIP, direction, quantity);
                position.upsertPositions(account_number, CUSIP, direction, quantity);
            }
        }
    }
}