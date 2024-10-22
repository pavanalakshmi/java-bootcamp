package org.pavani.multithreading.trading_multithreading.util;

import lombok.Getter;
import org.pavani.multithreading.trading_multithreading.exception.ConfigFileReadException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfigProperties {
    private static final String FILE_PATH = "/Users/akm/pavani/JavaBootcamp/resources/trade.properties";
    private static final String ERROR_MESSAGE = "Error while reading application.properties file";
    private static ApplicationConfigProperties instance;

    @Getter
    private String fileName;
    @Getter
    private int chunkSize;
    @Getter
    private String dbUserName;
    @Getter
    private String dbPasswords;
    @Getter
    private String dbUrl;
    @Getter
    private int maxRetryAttempts;
    @Getter
    private int chunkProcessorThreadPoolSize;
    @Getter
    private int tradeProcessorThreadPoolSize;
    @Getter
    private int tradeProcessorQueueCount;
    @Getter
    private String distributionLogicCriteria;
    @Getter
    private String persistenceTechnology;
    @Getter
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