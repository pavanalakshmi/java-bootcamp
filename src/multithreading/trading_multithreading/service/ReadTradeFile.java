package multithreading.trading_multithreading.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReadTradeFile {
    public int chunksCount;
    ChunkGenerator chunkGenerator;
    List<String> chunkFileNames;

    public ReadTradeFile( ) {
        chunksCount = loadChunkSizeFromConfigProperties();
        chunkGenerator = new ChunkGenerator();
    }

    public List<String> readCSVGenerateChunks (String filePath) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            fileReader.readLine();
            List<String> lines = new ArrayList<>();

            while ((line = fileReader.readLine()) != null) {
                lines.add(line); //totalRows++; // Count rows excluding header
            }
            int totalRows = lines.size();
            chunkFileNames = chunkGenerator.generateChunks(totalRows, lines, chunksCount);
        } catch (IOException e) {
            throw new RuntimeException("File read error: " + e.getMessage());
        }
        return chunkFileNames;
    }

    private int loadChunkSizeFromConfigProperties() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("/Users/akm/pavani/JavaBootcamp/resources/trade.properties")) {
            properties.load(input);
            try {
                String chunkCountString = properties.getProperty("chunks.count");
                chunksCount = Integer.parseInt(chunkCountString);
            } catch (NumberFormatException e) {
                System.out.println("Invalid chunks count in properties file.\"+\n" + " Fix the application.properties file before re-running the program ");
                System.exit(1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return chunksCount;
    }
}