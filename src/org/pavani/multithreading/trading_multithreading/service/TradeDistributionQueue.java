package org.pavani.multithreading.trading_multithreading.service;

import java.util.concurrent.ConcurrentMap;

public interface TradeDistributionQueue {
    void distributeQueue(String file, ConcurrentMap<String, String> resultMap);
    int getQueueIndex(ConcurrentMap<String, String> resultMap, String accNumber);
}