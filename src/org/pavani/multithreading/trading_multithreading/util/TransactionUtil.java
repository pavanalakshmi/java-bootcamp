package org.pavani.multithreading.trading_multithreading.util;

public interface TransactionUtil {
    void startTransaction();

    void commitTransaction();

    void rollbackTransaction();
}
