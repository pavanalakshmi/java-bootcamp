package multithreading.trading_multithreading.service;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.dao.InsertPayloadDAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ChunkProcessorService implements ChunkProcessor {
    private final ExecutorService executor;
    private final InsertPayloadDAO insertPayloadDAO;
    HikariDataSource dataSource;
    TradeDistributorMapService tradeDistributorMap;
    TradeDistributionQueueService tradeDistributionQueue;
    Map<String, LinkedBlockingQueue<String>> resultQueue;
    TradeProcessorService tradeProcessor;

    public ChunkProcessorService() {
        executor = Executors.newFixedThreadPool(10); // Create a thread pool of size 10
        insertPayloadDAO = new InsertPayloadDAO();
        dataSource = HikariCPConfig.getDataSource();
        tradeDistributorMap = new TradeDistributorMapService();
        tradeDistributionQueue = new TradeDistributionQueueService();
        resultQueue = new HashMap<>();
        tradeProcessor = new TradeProcessorService();
    }

    public void chunksProcessor(List<String> chunkFiles) {
        try {
            for (String file : chunkFiles) {
                executor.submit(() -> { // Submit chunk processing to the executor
                    try {
                        processChunk(file);
                        tradeDistributorMap.distributeMap(file); // size - 9992
                        tradeDistributionQueue.distributeQueue(file, tradeDistributorMap.getTradeMap());
                        tradeProcessor.processTrade(tradeDistributionQueue.getResultQueues());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error: "+e);
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();  // Forcibly terminate if needed
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            } finally {
                if (dataSource != null && !dataSource.isClosed()) {
                    dataSource.close();
                }
            }
        }
    }

    public void processChunk(String file) throws IOException {
        String line;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while ((line = fileReader.readLine()) != null) {
                insertPayloadDAO.insertIntoPayload(line);
            }
        }
    }
}

/**
 Submit distributeMap as a task to the executor and capture the Future
 Future<ConcurrentHashMap<String, String>> future = executor.submit(() ->
 try {
 resultMap = future.get();
 } catch (InterruptedException | ExecutionException e) {
 throw new RuntimeException(e);
 }
 **/
