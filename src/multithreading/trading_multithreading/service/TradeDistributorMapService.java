package multithreading.trading_multithreading.service;

import multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TradeDistributorMapService implements TradeDistributionMap {
    ConcurrentMap<String, String> tradeMap;
    private Random random;
    private int queueIndex;
    ApplicationConfigProperties applicationConfigProperties;
    private final List<String> listOfQueues;

    public ConcurrentMap<String, String> getTradeMap() {
        return tradeMap;
    }

    public TradeDistributorMapService() {
        tradeMap = new ConcurrentHashMap<>();
        listOfQueues = new ArrayList<>();
        random = new Random();
        applicationConfigProperties = new ApplicationConfigProperties();
        int queueCount = applicationConfigProperties.loadTradeProcessorQueueCount();
        queueIndex=1;
        for(int i=1;i<=queueCount;i++){
            listOfQueues.add("q"+i);
        }
    }

    public synchronized void distributeMapWithTradeId(String file){
        String line;
        String algorithm = applicationConfigProperties.loadAlgorithm();
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
//                System.out.println(tradeMap.size()); // since all tradeIds is unique
            }
        } catch (IOException e) {
            System.out.println("Error while reading file in distributeMap: "+e.getMessage());
        }
    }

    public synchronized void distributeMapWithAccountNumber(String file){
        String line;
        String algorithm = applicationConfigProperties.loadAlgorithm();
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
            System.out.println("Error while reading file in distributeMap: "+e.getMessage());
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