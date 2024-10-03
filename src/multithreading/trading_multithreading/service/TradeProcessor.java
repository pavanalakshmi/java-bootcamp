package multithreading.trading_multithreading.service;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public interface TradeProcessor {
    void processTrade(Map<String , LinkedBlockingQueue<String>> tradeDistributionQueuesMap);
}
