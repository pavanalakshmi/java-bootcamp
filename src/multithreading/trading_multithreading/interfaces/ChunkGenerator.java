package multithreading.trading_multithreading.interfaces;

import java.util.List;

public interface ChunkGenerator {
    void chunkGenerator(int rowsPerChunk, int totalRows, List<String> lines);
}
