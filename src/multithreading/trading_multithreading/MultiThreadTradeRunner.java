package multithreading.trading_multithreading;

import multithreading.trading_multithreading.service.ChunkProcessorService;
import multithreading.trading_multithreading.service.ReadTradeFile;
import multithreading.trading_multithreading.service.TradeDistributionQueueService;
import multithreading.trading_multithreading.service.TradeProcessorService;
import multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.util.concurrent.LinkedBlockingQueue;

public class MultiThreadTradeRunner {

    public static void main(String[] args) {
        ApplicationConfigProperties applicationConfigProperties = new ApplicationConfigProperties();
        String filePath = applicationConfigProperties.loadFilePath();

        LinkedBlockingQueue<String> chunkQueue = new LinkedBlockingQueue<>();
        TradeDistributionQueueService tradeDistributionQueueService = new TradeDistributionQueueService(applicationConfigProperties.loadTradeProcessorQueueCount());

        ReadTradeFile readTradeFile = new ReadTradeFile(chunkQueue);
        ChunkProcessorService chunkProcessor = new ChunkProcessorService(chunkQueue, tradeDistributionQueueService);
        TradeProcessorService tradeProcessorService = new TradeProcessorService(tradeDistributionQueueService.getResultQueues());

        try {
            Thread chunkProcessorThread = new Thread(chunkProcessor::chunksProcessor);
            chunkProcessorThread.start();
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
            e.printStackTrace();
        }
    }
}