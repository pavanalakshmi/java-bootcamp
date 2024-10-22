package org.pavani.multithreading.trading_multithreading;

import org.pavani.multithreading.trading_multithreading.service.ChunkProcessorService;
import org.pavani.multithreading.trading_multithreading.service.ReadTradeFile;
import org.pavani.multithreading.trading_multithreading.service.TradeDistributionQueueService;
import org.pavani.multithreading.trading_multithreading.service.TradeProcessorService;
import org.pavani.multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class MultiThreadTradeRunner {
    static Logger logger = Logger.getLogger(MultiThreadTradeRunner.class.getName());
    
    public static void main(String[] args) {
        ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getInstance();
        String filePath = applicationConfigProperties.getFileName();

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        LinkedBlockingQueue<String> chunkQueue = new LinkedBlockingQueue<>();
        TradeDistributionQueueService tradeDistributionQueueService = new TradeDistributionQueueService(applicationConfigProperties.getTradeProcessorQueueCount());

        ReadTradeFile readTradeFile = new ReadTradeFile(chunkQueue);
        ChunkProcessorService chunkProcessor = new ChunkProcessorService(chunkQueue, tradeDistributionQueueService);
        TradeProcessorService tradeProcessorService = new TradeProcessorService(tradeDistributionQueueService.getResultQueues());


        long startTime = System.currentTimeMillis();

        try {
            Thread chunkProcessorThread = new Thread(chunkProcessor::chunksProcessor);
            chunkProcessorThread.start();
            String time = "Timestamp: "+ localDateTime.format(formatter);
            logger.info(time);
            logger.info("Chunk Processor started >");

            Thread chunkGeneratorThread = new Thread(() -> readTradeFile.readCSVGenerateChunks(filePath));
            chunkGeneratorThread.start();
            logger.info("Chunk Generator started >");
            chunkGeneratorThread.join();
            chunkProcessorThread.join();

            Thread tradeProcessorThread = new Thread(tradeProcessorService::processTrade);
            tradeProcessorThread.start();
            logger.info("Trade Processor started >");
            tradeProcessorThread.join();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            String stackTrace = "Interrupted exception: " + e.getMessage();
            logger.info(stackTrace);
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime; // Duration in milliseconds

        String timeString = "Total execution time: " + (duration*0.001) + " seconds";
        logger.info(timeString);
    }
}