package org.pavani.multithreading.trading_multithreading.util;

import org.pavani.multithreading.trading_multithreading.exception.ConfigFileReadException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfigProperties {
    private static final String FILE_PATH = "/Users/akm/pavani/JavaBootcamp/resources/trade.properties";
    private static final String ERROR_MESSAGE = "Error while reading application.properties file";
    private static ApplicationConfigProperties instance;

    private String fileName;
    private int chunkSize;
    private String dbUserName;
    private String dbPasswords;
    private String dbUrl;
    private int maxRetryAttempts;
    private int chunkProcessorThreadPoolSize;
    private int tradeProcessorThreadPoolSize;
    private int tradeProcessorQueueCount;
    private String distributionLogicCriteria;
    private String persistenceTechnology;
    private String algorithm;
    private String useMap;
    private String useStoredProcedure;
    private String useRabbitMQ;

    private ApplicationConfigProperties() {
        loadProperties();
    }

    public static synchronized ApplicationConfigProperties getInstance() {
        if (instance == null) {
            instance = new ApplicationConfigProperties();
        }
        return instance;
    }

    private void loadProperties(){
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(FILE_PATH)) {
            properties.load(input);
            fileName = properties.getProperty("file.path");
            chunkSize = Integer.parseInt(properties.getProperty("chunks.count"));
            dbUserName = properties.getProperty("dbUserName");
            dbPasswords = properties.getProperty("dbPassword");
            dbUrl = properties.getProperty("dbUrl");
            maxRetryAttempts = Integer.parseInt(properties.getProperty("maxRetryCount"));
            chunkProcessorThreadPoolSize = Integer.parseInt(properties.getProperty("chunkProcessorThreadPoolSize"));
            tradeProcessorThreadPoolSize = Integer.parseInt(properties.getProperty("tradeProcessorThreadPoolSize"));
            tradeProcessorQueueCount = Integer.parseInt(properties.getProperty("tradeProcessorQueueCount"));
            distributionLogicCriteria = properties.getProperty("distributionLogic.criteria");
            persistenceTechnology = properties.getProperty("persistence.technology");
            algorithm = properties.getProperty("distributionMapLogic.algorithm");
            useMap = properties.getProperty("distributionLogic.useMap");
            useStoredProcedure = properties.getProperty("useStoredProcedure");
            useRabbitMQ = properties.getProperty("useRabbitMQ");


        } catch (IOException e) {
            throw new ConfigFileReadException(ERROR_MESSAGE+e.getMessage());
        }
    }


    public String getFileName() {
        return fileName;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public String getDbPasswords() {
        return dbPasswords;
    }

    public int getChunkProcessorThreadPoolSize() {
        return chunkProcessorThreadPoolSize;
    }

    public int getTradeProcessorThreadPoolSize() {
        return tradeProcessorThreadPoolSize;
    }

    public int getTradeProcessorQueueCount() {
        return tradeProcessorQueueCount;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public int getMaxRetryAttempts() {
        return maxRetryAttempts;
    }

    public String getDistributionLogicCriteria() {
        return distributionLogicCriteria;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getPersistenceTechnology() {
        return persistenceTechnology;
    }

    public Boolean getUseMap() {
        return useMap.equalsIgnoreCase("true");
    }

    public Boolean getUseStoredProcedure() {
        return useStoredProcedure.equalsIgnoreCase("true");
    }

    public Boolean getUseRabbitMQ() {
        return useRabbitMQ.equalsIgnoreCase("true");
    }

}