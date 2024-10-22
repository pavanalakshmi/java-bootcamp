package org.pavani.multithreading.trading_multithreading.service;

import org.pavani.multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class ReadTradeFile {
    public final int chunksCount;
    ChunkGeneratorService chunkGenerator;
    List<String> chunkFileNames;
    LinkedBlockingQueue<String> chunkQueue;
    private static final ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getInstance();
    Logger logger = Logger.getLogger(ReadTradeFile.class.getName());

    public ReadTradeFile( LinkedBlockingQueue<String> chunkQueue ) {
        chunksCount = applicationConfigProperties.getChunkSize();
        chunkGenerator = new ChunkGeneratorService();
        this.chunkQueue = chunkQueue;
    }

    public void readCSVGenerateChunks (String filePath) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            fileReader.readLine();
            List<String> lines = new ArrayList<>();

            while ((line = fileReader.readLine()) != null) {
                lines.add(line);
            }
            int totalRows = lines.size();
            chunkFileNames = chunkGenerator.generateChunks(totalRows, lines, chunksCount);
            for(String chunkFileName : chunkFileNames){
                try {
                    chunkQueue.put(chunkFileName);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.warning("Failed to add chunk to queue: "+e);
                }
            }
        } catch (IOException e) {
            logger.warning("File read error: " + e.getMessage());
        }
    }
}