package multithreading.trading_multithreading.service;

import multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TradeDistributorMapService implements TradeDistributionMap {
    static ConcurrentHashMap<String, String> tradeMap;
    private int queueIndex;
    ApplicationConfigProperties applicationConfigProperties;
    private List<String> listOfQueues;

    public ConcurrentHashMap<String, String> getTradeMap() {
        return tradeMap;
    }

    public TradeDistributorMapService() {
        tradeMap = new ConcurrentHashMap<>();
        listOfQueues = new ArrayList<>();
        applicationConfigProperties = new ApplicationConfigProperties();
        int queueCount = applicationConfigProperties.loadTradeProcessorQueueCount();
        queueIndex=1;
        for(int i=1;i<=queueCount;i++){
            listOfQueues.add("q"+i);
        }
    }

    public synchronized ConcurrentHashMap<String,String> distributeMap(String file){
        String line;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while ((line = fileReader.readLine()) != null) {
                String accountNumber = line.split(",")[2];
                if(!tradeMap.containsKey(accountNumber)){
                    String queue = getNextQueue();
                    tradeMap.put(accountNumber, queue);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tradeMap;
    }

    private String getNextQueue() {
        queueIndex = (queueIndex + 1) % listOfQueues.size();
        return "q"+(queueIndex+1);
    }
}