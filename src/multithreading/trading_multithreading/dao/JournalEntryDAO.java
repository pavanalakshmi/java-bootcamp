package multithreading.trading_multithreading.dao;

import multithreading.trading_multithreading.model.Trade;

public interface JournalEntryDAO {
    void insertToJournalEntry(Trade trade);
}
