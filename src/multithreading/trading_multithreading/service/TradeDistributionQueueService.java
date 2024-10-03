package multithreading.trading_multithreading.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class TradeDistributionQueueService {
    private LinkedBlockingQueue<String> q1;
    private LinkedBlockingQueue<String> q2;
    private LinkedBlockingQueue<String> q3;

    public Map<String, LinkedBlockingQueue<String>> getResultQueues() {
        return resultQueues;
    }

    Map<String, LinkedBlockingQueue<String>> resultQueues;

    public TradeDistributionQueueService() {
        q1 = new LinkedBlockingQueue<>();
        q2 = new LinkedBlockingQueue<>();
        q3 = new LinkedBlockingQueue<>();
        resultQueues = new HashMap<>();
    }

    public  Map<String, LinkedBlockingQueue<String>> distributeQueue(String file, ConcurrentHashMap<String, String> resultMap){
        // get acc number from concurrent hashmaps, but add corresponding trade_id to queues
        for(String accountNumber: resultMap.keySet()){
            String queueId = resultMap.get(accountNumber);
            String line;
            try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                while ((line = fileReader.readLine()) != null) {
                    String tradeId = line.split(",")[0];
                    String accountNumberFromLine = line.split(",")[2];

                    if(accountNumberFromLine.equals(accountNumber)){
                        switch (queueId){
                            case "q1":
                                q1.add(tradeId); //q1.offer(tradeId) If the queue has a capacity limit and is full, offer() returns false instead of throwing an exception like add
                                break;
                            case "q2":
                                q2.add(tradeId);
                                break;
                            case "q3":
                                q3.add(tradeId);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: "+queueId);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
//        System.out.println(q1.size()); //3335
//        System.out.println(q2.size()); //3333
//        System.out.println(q3.size()); //3332
        resultQueues.put("q1", q1);
        resultQueues.put("q2", q2);
        resultQueues.put("q3", q3);

//        LinkedBlockingQueue<String> mergedQueue = new LinkedBlockingQueue<>();
//        mergedQueue.addAll(q1);
//        mergedQueue.addAll(q2);
//        mergedQueue.addAll(q3);
        return resultQueues;
    }
}