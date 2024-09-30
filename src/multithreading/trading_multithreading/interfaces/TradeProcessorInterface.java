package multithreading.trading_multithreading.interfaces;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public interface TradeProcessorInterface {
    void processTrade(Map<String , LinkedBlockingQueue<String>> tradeDistributionQueuesMap);
}
