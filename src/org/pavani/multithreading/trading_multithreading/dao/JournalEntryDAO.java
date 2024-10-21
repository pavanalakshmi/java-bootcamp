package org.pavani.multithreading.trading_multithreading.dao;

import org.pavani.multithreading.trading_multithreading.model.Trade;

public interface JournalEntryDAO {
    void insertToJournalEntry(Trade trade);
}
