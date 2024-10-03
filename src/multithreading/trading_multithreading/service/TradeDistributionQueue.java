package multithreading.trading_multithreading.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public interface TradeDistributionQueue {
    Map<String , LinkedBlockingQueue<String>> distributeQueue(List<String> lines, ConcurrentHashMap<String, String> resultMap);
    }
