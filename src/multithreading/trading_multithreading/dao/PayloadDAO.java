package multithreading.trading_multithreading.dao;

public interface PayloadDAO {
    void insertIntoPayload(String line);
    void updatePayload(String tradeId, String newStatus);
}
