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
    //    private Random random;
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
//        random = new Random();

        queueIndex=1; //Initialize the index for round-robin assignment

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
//                String randomQueue = getRandomQueue();
                    String queue = getNextQueue();
                    tradeMap.put(accountNumber, queue);
//                tradeMap.put(accountNumber, randomQueue);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//                System.out.println("trademap size after processing:"+tradeMap.size()); // 9992
        return tradeMap;
    }

    private String getNextQueue() {
        // Round robin distribution so that each q gets equal values
//        String queue;
        // Determine which queue to assign based on the current index
//        switch (queueIndex) {
//            case 0:
//                queue = "q1";
//                break;
//            case 1:
//                queue = "q2";
//                break;
//            case 2:
//            default:
//                queue = "q3";
//                break;
//        }
        // Increment the index and wrap around if necessary
        queueIndex = (queueIndex + 1) % listOfQueues.size();
//        String queue = listOfQueues.get(queueIndex);
        return "q"+(queueIndex+1);
    }

//    private String getRandomQueue() {
//        int randomIndex = random.nextInt(3); // 0-2
//        switch (randomIndex){
//            case 0:
//                return "q1";
//            case 1:
//                return "q2";
//            case 2:
//            default:
//                return "q3";
//        }
//    }

}