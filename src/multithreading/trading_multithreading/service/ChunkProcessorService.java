package multithreading.trading_multithreading.service;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.dao.InsertPayloadDAO;
import multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ChunkProcessorService implements ChunkProcessor {
    private final ExecutorService executor;
    private final InsertPayloadDAO insertPayloadDAO;
    HikariDataSource dataSource;
    TradeDistributorMapService tradeDistributorMap;
    TradeDistributionQueueService tradeDistributionQueue;
    static ApplicationConfigProperties applicationConfigProperties = new ApplicationConfigProperties();
    private final LinkedBlockingQueue<String> chunkQueue;

    public ChunkProcessorService(LinkedBlockingQueue<String> chunkQueue, TradeDistributionQueueService tradeDistributionQueueService) {
        this.chunkQueue = chunkQueue;
        executor = Executors.newFixedThreadPool(applicationConfigProperties.loadChunkProcessorThreadPoolSize()); // Create a thread pool of size 10
        insertPayloadDAO = new InsertPayloadDAO();
        dataSource = HikariCPConfig.getDataSource();
        tradeDistributorMap = new TradeDistributorMapService();
        this.tradeDistributionQueue = tradeDistributionQueueService;
    }

    public void chunksProcessor() {
        try {
            while (true){
                String file = chunkQueue.poll(60, TimeUnit.SECONDS);
                if(file==null){
                    break;
                }
                executor.submit(() -> { // Submit chunk processing to the executor
                    try {
                        processChunk(file);
                        tradeDistributorMap.distributeMap(file); // size - 9992
                        tradeDistributionQueue.distributeQueue(file, tradeDistributorMap.getTradeMap());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
//        finally {
//            executor.shutdown();
//            try {
//                if (!executor.awaitTermination(20, TimeUnit.SECONDS)) {
//                    executor.shutdownNow();  // Forcibly terminate if needed
//                }
//            } catch (InterruptedException e) {
//                executor.shutdownNow();
//                Thread.currentThread().interrupt();
//            }
//                if (dataSource != null && !dataSource.isClosed()) {
//                    dataSource.close();
//                }
//        }
//        System.out.println("Map SIZE :"+tradeDistributorMap.getTradeMap().size());
//        System.out.println("RES QUEUE :"+tradeDistributionQueue.getResultQueues());
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