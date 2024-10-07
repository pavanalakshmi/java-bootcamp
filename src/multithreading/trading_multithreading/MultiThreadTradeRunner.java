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

            Thread chunkGeneratorThread = new Thread(() -> readTradeFile.readCSVGenerateChunks(filePath));
            chunkGeneratorThread.start();

            chunkGeneratorThread.join();
            chunkProcessorThread.join();

            Thread tradeProcessorThread = new Thread(tradeProcessorService::processTrade);
            tradeProcessorThread.start();
            tradeProcessorThread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// chunk generator and chunk processor in parallel - doing
// if error in trade processor when inserting to positions table,
// send the acc number back to kafka queue; and create a local hashmap with acc numberm retry count;
// if retry count 3; dead letter queue ; don't try again
// versions
// record

/**
 * asynchronous; synchronize
 * coupling
 * Threads
 * Record
 */



