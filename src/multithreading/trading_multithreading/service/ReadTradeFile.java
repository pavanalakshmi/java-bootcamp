package multithreading.trading_multithreading.service;

import multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ReadTradeFile {
    public final int chunksCount;
    ChunkGeneratorService chunkGenerator;
    List<String> chunkFileNames;
    LinkedBlockingQueue<String> chunkQueue;
    private static ApplicationConfigProperties applicationConfigProperties;

    public ReadTradeFile( LinkedBlockingQueue<String> chunkQueue ) {
        applicationConfigProperties = ApplicationConfigProperties.getInstance();
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
                    throw new RuntimeException("Failed to add chunk to queue: "+e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("File read error: " + e.getMessage());
        }
    }
}