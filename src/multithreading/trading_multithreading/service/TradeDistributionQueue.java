package multithreading.trading_multithreading.service;

import java.util.concurrent.ConcurrentHashMap;

public interface TradeDistributionQueue {
    void distributeQueue(String file, ConcurrentHashMap<String, String> resultMap);
    int getQueueIndex(ConcurrentHashMap<String, String> resultMap, String accNumber);
}