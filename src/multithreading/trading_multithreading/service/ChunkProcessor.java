package multithreading.trading_multithreading.service;

import java.io.IOException;
import java.util.List;

public interface ChunkProcessor {
    void chunksProcessor(List<String> chunkData);
    void processChunk(String file) throws IOException;
}
