package org.pavani.multithreading.trading_multithreading.service;

import lombok.Getter;
import org.pavani.multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class TradeDistributorMapService implements TradeDistributionMap {
    @Getter
    ConcurrentMap<String, String> tradeMap;
    private final Random random;
    private int queueIndex;
    private static final ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getInstance();
    private final List<String> listOfQueues;
    Logger logger = Logger.getLogger(TradeDistributorMapService.class.getName());

    public TradeDistributorMapService() {
        tradeMap = new ConcurrentHashMap<>();
        listOfQueues = new ArrayList<>();
        random = new Random();
        int queueCount = applicationConfigProperties.getTradeProcessorQueueCount();
        queueIndex=1;
        for(int i=1;i<=queueCount;i++){
            listOfQueues.add("q"+i);
        }
    }

    public synchronized void distributeMapWithTradeId(String file){
        String line;
        String algorithm = applicationConfigProperties.getAlgorithm();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while ((line = fileReader.readLine()) != null) {
                String tradeId = line.split(",")[0];
                if(!tradeMap.containsKey(tradeId)){
                    if(algorithm.equals("round-robin")){
                        String queue = getNextQueue();
                        tradeMap.put(tradeId, queue);
                    } else if(algorithm.equals("random")){
                        String randomQueue = getRandomQueue();
                        tradeMap.put(tradeId, randomQueue);
                    }
                }
            }
        } catch (IOException e) {
            logger.info("Error while reading file in distributeMap: "+e.getMessage());
        }
    }

    public synchronized void distributeMapWithAccountNumber(String file){
        String line;
        String algorithm = applicationConfigProperties.getAlgorithm();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while ((line = fileReader.readLine()) != null) {
                String accountNumber = line.split(",")[2];
                if(!tradeMap.containsKey(accountNumber)){
                    if(algorithm.equals("round-robin")){
                        String queue = getNextQueue();
                        tradeMap.put(accountNumber, queue);
                    } else if(algorithm.equals("random")){
                        String randomQueue = getRandomQueue();
                        tradeMap.put(accountNumber, randomQueue);
                    }
                }
            }
        } catch (IOException e) {
            logger.info("Error while reading file in distributeMap: "+e.getMessage());
        }
    }

    private String getNextQueue() {
        queueIndex = (queueIndex + 1) % listOfQueues.size();
        return "q"+(queueIndex+1);
    }

    private String getRandomQueue() {
        int randomIndex = random.nextInt(listOfQueues.size());
        return "q" + (randomIndex + 1);
    }
}