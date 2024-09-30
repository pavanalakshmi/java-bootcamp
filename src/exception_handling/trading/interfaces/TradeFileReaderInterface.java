package exception_handling.trading.interfaces;

import exception_handling.trading.exception.HitErrorsThresholdException;

public interface TradeFileReaderInterface {
    void readTradeDataFromCSV(String filePath) throws HitErrorsThresholdException;
}
