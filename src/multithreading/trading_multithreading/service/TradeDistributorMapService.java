package multithreading.trading_multithreading.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class TradeDistributorMapService implements TradeDistributionMap {
    static ConcurrentHashMap<String, String> tradeMap;
//    private Random random;
    private int queueIndex;

    public ConcurrentHashMap<String, String> getTradeMap() {
        return tradeMap;
    }

    public TradeDistributorMapService() {
        tradeMap = new ConcurrentHashMap<>();
//        random = new Random();
        queueIndex=0; //Initialize the index for round-robin assignment
    }

    public ConcurrentHashMap<String,String> distributeMap(String file){
        String line;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while ((line = fileReader.readLine()) != null) {
                String accountNumber = line.split(",")[2];
                if(!tradeMap.contains(accountNumber)){
//                String randomQueue = getRandomQueue();
                    String queue = getNextQueue();
                    tradeMap.put(accountNumber, queue);
//                tradeMap.put(accountNumber, randomQueue);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } //        System.out.println(tradeMap.size()); // 9992
        return tradeMap;
    }

    private String getNextQueue() {
        // Round robin distribution so that each q gets equal values
        String queue;
        // Determine which queue to assign based on the current index
        switch (queueIndex) {
            case 0:
                queue = "q1";
                break;
            case 1:
                queue = "q2";
                break;
            case 2:
            default:
                queue = "q3";
                break;
        }
        // Increment the index and wrap around if necessary
        queueIndex = (queueIndex + 1) % 3;
        return queue;
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
