package multithreading.trading_multithreading.interfaces;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public interface TradeDistributionQueueInterface {
    Map<String , LinkedBlockingQueue<String>> distributeQueue(List<String> lines, ConcurrentHashMap<String, String> resultMap);
    }
