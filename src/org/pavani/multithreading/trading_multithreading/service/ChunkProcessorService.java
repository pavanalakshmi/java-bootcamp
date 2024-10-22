package org.pavani.multithreading.trading_multithreading.service;

import com.zaxxer.hikari.HikariDataSource;
import org.pavani.multithreading.trading_multithreading.config.HikariCPConfig;
import org.pavani.multithreading.trading_multithreading.dao.PayloadDAO;
import org.pavani.multithreading.trading_multithreading.factory.BeanFactory;
import org.pavani.multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ChunkProcessorService implements ChunkProcessor {
    private final ExecutorService chunkProcessorExecutorService;
    HikariDataSource dataSource;
    TradeDistributorMapService tradeDistributorMap;
    TradeDistributionQueueService tradeDistributionQueue;
    private static final ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getInstance();
    private final LinkedBlockingQueue<String> chunkQueue;
    private static final PayloadDAO payloadDAO = BeanFactory.getPayloadDAO();
    Logger logger = Logger.getLogger(ChunkProcessorService.class.getName());

    public ChunkProcessorService(LinkedBlockingQueue<String> chunkQueue, TradeDistributionQueueService tradeDistributionQueueService) {
        this.chunkQueue = chunkQueue;
        chunkProcessorExecutorService = Executors.newFixedThreadPool(applicationConfigProperties.getChunkProcessorThreadPoolSize()); // Create a thread pool of size 10
        dataSource = HikariCPConfig.getDataSource();
        tradeDistributorMap = new TradeDistributorMapService();
        this.tradeDistributionQueue = tradeDistributionQueueService;
    }

    public void chunksProcessor() {
        try {
            int emptyPollCount = 0;
            int maxEmptyPolls = 5;
            while (true){
                String file = chunkQueue.poll(500, TimeUnit.MILLISECONDS);
                if(file==null){
                    emptyPollCount++;
                    if (emptyPollCount >= maxEmptyPolls) {
                        logger.info("No more files to process, exiting...");
                        break;
                    }
                    continue;
                }
                    emptyPollCount = 0;
                    submitFileProcessingTask(file);
            }
        } catch (Exception e) {
            logger.info("Error in chunk processor: " + e.getMessage());
        }
        finally {
            chunkProcessorExecutorService.shutdown();
            try {
                if (!chunkProcessorExecutorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                    chunkProcessorExecutorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                chunkProcessorExecutorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
            if (dataSource != null && !dataSource.isClosed()) {
                dataSource.close();
            }
        }
    }

    private void submitFileProcessingTask(String file) {
        chunkProcessorExecutorService.submit(() -> {
            try {
                processChunk(file);
                distributeFileBasedOnCriteria(file);
            } catch (IOException e) {
                logger.info("Exception occurred in chunks processor");
            }
        });
    }

    private void distributeFileBasedOnCriteria(String file) {
        String criteria = applicationConfigProperties.getDistributionLogicCriteria();
        if(Boolean.TRUE.equals(applicationConfigProperties.getUseMap())){
            if(criteria.equals("tradeId")){ //10k
                tradeDistributorMap.distributeMapWithTradeId(file); // size - 9992
            } else if (criteria.equals("accountNumber")) {  // 9992
                tradeDistributorMap.distributeMapWithAccountNumber(file);
            }
            tradeDistributionQueue.distributeQueue(file, tradeDistributorMap.getTradeMap());
        } else{
            tradeDistributionQueue.distributeQueueWithoutMap(file);
        }
    }

    public void processChunk(String file) throws IOException {
        String line;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while ((line = fileReader.readLine()) != null) {
                payloadDAO.insertIntoPayload(line);
            }
        }
    }
}