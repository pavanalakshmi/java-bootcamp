package multithreading.trading_multithreading.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class TradeDistributionQueueService implements TradeDistributionQueue {
    /*    private LinkedBlockingQueue<String> q1;
        private LinkedBlockingQueue<String> q2;
        private LinkedBlockingQueue<String> q3;*/
//    LinkedBlockingQueue<String>[] resultQueues;
    private final List<LinkedBlockingQueue<String>> queues;

    Map<String, LinkedBlockingQueue<String>> resultQueues;

    public TradeDistributionQueueService(int numberOfQueues) {
        queues = new ArrayList<>(numberOfQueues);
/*        q1 = new LinkedBlockingQueue<>();
        q2 = new LinkedBlockingQueue<>();
        q3 = new LinkedBlockingQueue<>();*/
        resultQueues = new HashMap<>();
        for (int i = 0; i < numberOfQueues; i++) {
            LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
            queues.add(queue);
            resultQueues.put("q" + (i + 1), queue);
        }
    }

    public Map<String, LinkedBlockingQueue<String>> getResultQueues() {
        return resultQueues;
    }


    public int getQueueIndex(ConcurrentHashMap<String, String> resultMap, String accNumber) {
        int queueIndex = 1;
        if(resultMap.containsKey(accNumber)) {
            queueIndex = Integer.parseInt(resultMap.get(accNumber).substring(1))-1;
        }
        return queueIndex;
    }

    public synchronized void distributeQueue(String file, ConcurrentHashMap<String, String> resultMap) {
        String line;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while ((line = fileReader.readLine()) != null) {
                String tradeId = line.split(",")[0];
                String accNumber = line.split(",")[2];
                int queueIndex = getQueueIndex(resultMap,accNumber);
                if (queueIndex >= 0 && queueIndex < queues.size()) {
                    queues.get(queueIndex).add(tradeId);
                } else {
                    System.err.println("Invalid queue index: " + queueIndex + " for trade ID: " + tradeId);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Map<String, LinkedBlockingQueue<String>> distributeQueue1(String file, ConcurrentHashMap<String, String> resultMap) {
        // get acc number from concurrent hashmaps, but add corresponding trade_id to queues
//        for (String accountNumber : resultMap.keySet()) {
//        int queueIndex = getQueueIndex(resultMap);
        int queueIndex = 0;
        String line;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while ((line = fileReader.readLine()) != null) {
                System.out.println(line);
                String tradeId = line.split(",")[0];
                System.out.println("trade id :" + tradeId);
                String accountNumberFromLine = line.split(",")[2];

//                    if (accountNumberFromLine.equals(accountNumber)) {

                queues.get(queueIndex).add(tradeId);
//                        switch (queueId){
//                            case "q1":
//                                q1.add(tradeId); //q1.offer(tradeId) If the queue has a capacity limit and is full, offer() returns false instead of throwing an exception like add
//                                break;
//                            case "q2":
//                                q2.add(tradeId);
//                                break;
//                            case "q3":
//                                q3.add(tradeId);
//                                break;
//                            default:
//                                throw new IllegalStateException("Unexpected value: "+queueId);
//                        }
//                    }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        }
//        System.out.println(q1.size()); //3335
//        System.out.println(q2.size()); //3333
//        System.out.println(q3.size()); //3332
//        resultQueues.put("q1", q1);
//        resultQueues.put("q2", q2);
//        resultQueues.put("q3", q3);
//        LinkedBlockingQueue<String> mergedQueue = new LinkedBlockingQueue<>();
//        mergedQueue.addAll(q1);
//        mergedQueue.addAll(q2);
//        mergedQueue.addAll(q3);
        return resultQueues;
    }
}