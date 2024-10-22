package org.pavani.multithreading.trading_multithreading.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ChunkGeneratorService {
    ExecutorService chunkGeneratorExecutorService = Executors.newSingleThreadExecutor();
    private final List<String> chunksDataFileNames = new ArrayList<>();
    Logger logger = Logger.getLogger(ChunkGeneratorService.class.getName());

    public List<String> generateChunks(int totalRows, List<String> lines, int chunksCount){
        int rowsPerChunk = totalRows / chunksCount;
        int remainingRows = totalRows % chunksCount;  // Remainder that needs to go into the last chunk
        for (int chunkIndex = 1; chunkIndex <= chunksCount; chunkIndex++) {
            final int index = chunkIndex;
            int start = (index-1) * rowsPerChunk;
            int end = index * rowsPerChunk;
            if (index == chunksCount) {
                end +=remainingRows;
            }
            end = Math.min(end, totalRows);

            List<String> chunkData = new ArrayList<>();
            for (int i = start; i <end; i++) {
                chunkData.add(lines.get(i));
            }
            try {
                chunkGeneratorExecutorService.submit(() -> {
                    try {
                        writeChunkToFile(chunkData, index);
                    } catch (Exception e) {
                        logger.warning("Error writing chunk: " + e);
                    }
                });
            } catch (Exception e) {
                logger.info("Error in submitting ChunkGeneratorExecutorService: "+e);
            }
        }
        chunkGeneratorExecutorService.shutdown();
        try {
            chunkGeneratorExecutorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Wait for all tasks to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warning("Task interrupted: " + e.getMessage());
        }
        return chunksDataFileNames;
    }

    private synchronized void writeChunkToFile(List<String> lines, int index) throws IOException {
        String chunkFileName = "/Users/akm/pavani/JavaBootcamp/resources/trade_chunks/trade_chunk" + index + ".csv";
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(chunkFileName))) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }
        synchronized (chunksDataFileNames){
            chunksDataFileNames.add(chunkFileName);
        }
    }
}
