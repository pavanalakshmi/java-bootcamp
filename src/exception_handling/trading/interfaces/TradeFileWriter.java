package exception_handling.trading.interfaces;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.sql.Connection;

public interface TradeFileWriter {
    void tradeFileWriter(BufferedReader fileReader, Connection connection, FileWriter errorLog);
}
