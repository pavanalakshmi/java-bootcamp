package org.pavani.multithreading.trading_multithreading.service;

import java.io.IOException;

public interface ChunkProcessor {
    public void chunksProcessor();
    void processChunk(String file) throws IOException;
}