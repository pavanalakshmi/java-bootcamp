package org.pavani.multithreading.trading_multithreading.service;

import java.util.List;

public interface ChunkGenerator {
    void chunkGenerator(int rowsPerChunk, int totalRows, List<String> lines);
}
