package multithreading.trading_multithreading.service;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.dao.InsertJournalEntryDAO;
import multithreading.trading_multithreading.dao.ReadPayloadDAO;
import multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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
    private final Map<String, Integer> retryMap;
    private final LinkedBlockingQueue<String> deadLetterQueue;

    public TradeProcessorService(Map<String, LinkedBlockingQueue<String>> queuesMap) {
        dataSource = HikariCPConfig.getDataSource();
        position = new Position();
        readPayloadDAO = new ReadPayloadDAO();
        insertJournalEntryDAO = new InsertJournalEntryDAO();
        queueCount = applicationConfigProperties.loadTradeProcessorQueueCount();
        map = queuesMap;
        executor = Executors.newFixedThreadPool(applicationConfigProperties.loadTradeProcessorThreadPoolSize());
        maxRetryCount = applicationConfigProperties.loadMaxRetryAttempts();
        retryMap = new ConcurrentHashMap<>();
        deadLetterQueue = new LinkedBlockingQueue<>();
    }

    public void processTrade() {
            for (Map.Entry<String, LinkedBlockingQueue<String>> entry : map.entrySet()) {
                LinkedBlockingQueue<String> queue = entry.getValue();
            executor.submit(() -> {
                try(Connection connection = dataSource.getConnection()) {
                    processTradeQueue(queue, connection);
                } catch (InterruptedException | SQLException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Error while processing queue: "+e.getMessage());
                }
            });
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(35, TimeUnit.SECONDS)) {
//            if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void processTradeQueue(LinkedBlockingQueue<String> queue, Connection connection) throws InterruptedException {
        String tradeId;
        List<String> tradesBatch = new ArrayList<>();
        int batchSize = 2000;
        int emptyPollCount = 0;
        int maxEmptyPolls = 3;

        while (true) {
            try {
//                tradeId = queue.poll(2, TimeUnit.SECONDS);
                tradeId = queue.poll(500, TimeUnit.MILLISECONDS);
                if (tradeId == null) {
                    emptyPollCount++;
                    if (emptyPollCount >= maxEmptyPolls && tradesBatch.isEmpty()) {
                        break;
                    }
                    continue;
                }
                emptyPollCount = 0;
                if (tradeId.equals("END")) {
                    if (!tradesBatch.isEmpty()) {
                        executeBatchTrades(tradesBatch, connection);
                    }
                    System.out.println("Batch processing ended.");
                    break;
                }
                tradesBatch.add(tradeId);
                if (tradesBatch.size() == batchSize) {
                    executeBatchTrades(tradesBatch, connection);
                    tradesBatch.clear();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted: " + e.getMessage());
                break;
            }
        }
    }

    private void executeBatchTrades(List<String> tradesBatch, Connection connection) {
        try (CallableStatement callableStatement = connection.prepareCall("{CALL INSERT_JOURNAL_UPDATE_POSITION(?, ?, ?, ?, ?, ?)}")) {
            String accountNumber = null;
            for (String tradeId : tradesBatch) {
                String payload = readPayloadDAO.readPayload(tradeId);
                String[] payloadData = payload.split(",");
                accountNumber = payloadData[2];
                String cusip = payloadData[3];
                String direction = payloadData[4];
                int quantity = Integer.parseInt(payloadData[5]);

                if (readPayloadDAO.isValidCUSIPSymbol(cusip)) {
                    callableStatement.setString(1, accountNumber);
                    callableStatement.setString(2, cusip);
                    callableStatement.setString(3, direction);
                    callableStatement.setInt(4, quantity);
                    callableStatement.setString(5, tradeId);
                    callableStatement.registerOutParameter(6, Types.VARCHAR);
                    callableStatement.execute();
                    String statusCode = callableStatement.getString(6);
                    System.out.println("Stored procedure result: " + statusCode);

                    switch (statusCode){
                        case "POSITION_UPDATE_DONE":
                            System.out.println("Trade successfully processed: " + tradeId);
                            break;
                        case "POSITION_INSERT_FAILED":
                            System.out.println("Trade Position Insert Failed : " + tradeId);
                            break;
                        case "JE_INSERT_FAILED", "POSITION_UPDATE_FAILED_OPTIMISTIC_LOCKING":
                            retryOrDeadLetterQueue(statusCode, accountNumber, tradeId);
                            break;
                        default:
                            throw new RuntimeException("Unexpected status from SP: " + statusCode);
                    }
                } else {
                    System.out.println("Invalid CUSIP: "+cusip);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void retryOrDeadLetterQueue(String statusCode, String accountNumber, String tradeId){
        int retryCount = retryMap.getOrDefault(accountNumber, 0);
        if(retryCount < maxRetryCount){
            retryCount++;
            retryMap.put(accountNumber, retryCount);
            System.out.println("Retrying trade: "+tradeId+" due to "+statusCode);
        } else {
            System.err.println("Max retries reached for trade: "+tradeId+", sending to DLQ");
            if(!deadLetterQueue.offer(tradeId)){
                System.err.println("Failed to add trade to DLQ: "+tradeId);
            }
        }
    }

    private boolean processTradeQueueVersion0(LinkedBlockingQueue<String> queue) throws InterruptedException, SQLException {
        String tradeId;
        String accountNumber;
        String cusip;
        String direction;
        int quantity;
        while ((tradeId = queue.poll(2, TimeUnit.SECONDS)) != null) {
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