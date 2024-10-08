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
    private final List<LinkedBlockingQueue<String>> queues;
    Map<String, LinkedBlockingQueue<String>> resultQueues;

    public TradeDistributionQueueService(int numberOfQueues) {
        queues = new ArrayList<>(numberOfQueues);
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
}