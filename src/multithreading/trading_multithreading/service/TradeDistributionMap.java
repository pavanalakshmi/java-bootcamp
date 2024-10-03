package multithreading.trading_multithreading.service;

import java.util.concurrent.ConcurrentHashMap;

public interface TradeDistributionMap {
    ConcurrentHashMap<String,String> distributeMap(String file);
}
