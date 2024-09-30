package multithreading.trading_multithreading;

import multithreading.trading_multithreading.service.ChunkProcessor;
import multithreading.trading_multithreading.service.ReadTradeFile;

import java.util.List;

public class Main {
    private static final String filePath = "/Users/akm/pavani/JavaBootcamp/resources/trades.csv";

    public static void main(String[] args) {
        ReadTradeFile readTradeFile = new ReadTradeFile();
        ChunkProcessor chunkProcessor = new ChunkProcessor();

        List<String> chunkFileNames = readTradeFile.readCSVGenerateChunks(filePath);
        chunkProcessor.chunksProcessor(chunkFileNames);

    }
}
