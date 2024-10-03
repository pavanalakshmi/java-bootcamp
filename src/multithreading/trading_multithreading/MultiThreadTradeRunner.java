package multithreading.trading_multithreading;

import multithreading.trading_multithreading.service.ChunkProcessorService;
import multithreading.trading_multithreading.service.ReadTradeFile;

import java.util.List;

public class MultiThreadTradeRunner {
    private static final String filePath = "/Users/akm/pavani/JavaBootcamp/resources/trades.csv";

    public static void main(String[] args) {
        ReadTradeFile readTradeFile = new ReadTradeFile();
        ChunkProcessorService chunkProcessor = new ChunkProcessorService();

        List<String> chunkFileNames = readTradeFile.readCSVGenerateChunks(filePath);
        chunkProcessor.chunksProcessor(chunkFileNames);
    }
}


// chunk generator and chunk processor in parallel
// all in config properties

// if error in trade processor when inserting to positions table,
//semd the acc number back to kafka queue; and create a local hashmap with acc numberm retry count;
// if retry count 3; deadletter queue ; dont try again


// versions

/**
 * asynchronous; synchronize
 * coupling
 * Threads
 *
 */



