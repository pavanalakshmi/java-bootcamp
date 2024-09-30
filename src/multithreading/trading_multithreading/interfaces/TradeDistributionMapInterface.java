package multithreading.trading_multithreading.interfaces;

import java.util.concurrent.ConcurrentHashMap;

public interface TradeDistributionMapInterface {
    ConcurrentHashMap<String,String> distributeMap(String file);
}
