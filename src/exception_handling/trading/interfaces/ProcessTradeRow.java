package exception_handling.trading.interfaces;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public interface ProcessTradeRow {
    boolean processTradeRow(String[] tradeData, PreparedStatement insertStatement, Connection connection, FileWriter errorLog) throws IOException;
    }
