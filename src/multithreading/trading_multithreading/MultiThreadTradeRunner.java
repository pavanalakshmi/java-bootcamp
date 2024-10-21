package multithreading.trading_multithreading;

import multithreading.trading_multithreading.service.ChunkProcessorService;
import multithreading.trading_multithreading.service.ReadTradeFile;
import multithreading.trading_multithreading.service.TradeDistributionQueueService;
import multithreading.trading_multithreading.service.TradeProcessorService;
import multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.LinkedBlockingQueue;

public class MultiThreadTradeRunner {

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
            System.out.println("Timestamp: "+ localDateTime.format(formatter));
            System.out.println("Chunk Processor started >");

            Thread chunkGeneratorThread = new Thread(() -> readTradeFile.readCSVGenerateChunks(filePath));
            chunkGeneratorThread.start();
            System.out.println("Chunk Generator started >");
            chunkGeneratorThread.join();
            chunkProcessorThread.join();

            Thread tradeProcessorThread = new Thread(tradeProcessorService::processTrade);
            tradeProcessorThread.start();
            System.out.println("Trade Processor started >");
            tradeProcessorThread.join();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime; // Duration in milliseconds

        System.out.println("Total execution time: " + (duration*0.001) + " seconds");
    }
}