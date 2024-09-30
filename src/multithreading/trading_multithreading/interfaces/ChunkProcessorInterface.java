package multithreading.trading_multithreading.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ChunkProcessorInterface {
    void chunksProcessor(List<String> chunkData);
    void processChunk(String file) throws IOException;
}
