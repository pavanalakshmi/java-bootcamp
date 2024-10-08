package multithreading.trading_multithreading.util;

import multithreading.trading_multithreading.exception.ConfigFileReadException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfigProperties {
    private static final String FILE_PATH = "/Users/akm/pavani/JavaBootcamp/resources/trade.properties";
    private static final String ERROR_MESSAGE = "Error while reading application.properties file ";

    private Properties loadProperties(){
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(FILE_PATH)) {
            properties.load(input);
        } catch (IOException e) {
            throw new ConfigFileReadException(ERROR_MESSAGE+e.getMessage());
        }
        return properties;
    }

    public String loadFilePath() {
        Properties properties = loadProperties();
        return properties.getProperty("file.path");
    }

    public int loadChunkSize() {
        Properties properties = loadProperties();
        return Integer.parseInt(properties.getProperty("chunks.count"));
    }

    public String loadDbUserName() {
        Properties properties = loadProperties();
        return properties.getProperty("dbUserName");
    }

    public String loadDbPasswords() {
        Properties properties = loadProperties();
        return properties.getProperty("dbPassword");
    }

    public String loadDbUrlFromConfigProperties() {
        Properties properties = loadProperties();
        return properties.getProperty("dbUrl");
    }

    public int loadMaxRetryAttempts() {
        Properties properties = loadProperties();
        return Integer.parseInt(properties.getProperty("maxRetryCount"));
    }

    public int loadChunkProcessorThreadPoolSize() {
        Properties properties = loadProperties();
        return Integer.parseInt(properties.getProperty("chunkProcessorThreadPoolSize"));
    }

    public int loadTradeProcessorThreadPoolSize() {
        Properties properties = loadProperties();
        return Integer.parseInt(properties.getProperty("tradeProcessorThreadPoolSize"));
    }

    public int loadTradeProcessorQueueCount() {
        Properties properties = loadProperties();
        return Integer.parseInt(properties.getProperty("tradeProcessorQueueCount"));
    }

    public boolean loadUseMap(){
        Properties properties = loadProperties();
        return Boolean.getBoolean(properties.getProperty("distributionLogic.useMap"));
    }
}

/**
public int loadTradeProcessorQueueCountFromConfigProperties() {
    Properties properties = new Properties();
    int tradeProcessorQueueCount = 0;
    try (InputStream input = new FileInputStream(FILE_PATH)) {
        properties.load(input);
        try {
            String tradeProcessorQueueCountString = properties.getProperty("tradeProcessorQueueCount");
            tradeProcessorQueueCount = Integer.parseInt(tradeProcessorQueueCountString);
        } catch (NumberFormatException e) {
            System.out.println("Invalid chunks count in properties file.\"+\n" + " Fix the application.properties file before re-running the program ");
            System.exit(1);
        }
    } catch (IOException e) {
        throw new ConfigFileReadException(ERROR_MESSAGE+e.getMessage());
    }
    return tradeProcessorQueueCount;
}
**/