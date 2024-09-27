package exception_handling.trading.service;

import exception_handling.trading.util.TickerValidator;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import static exception_handling.trading.Main.MAX_RETRY_ATTEMPTS;
import static exception_handling.trading.Main.validateTradeData;

public class TradeProcessorService {
    public static int dbWriter(PreparedStatement insertStatement, Connection connection) throws SQLException {
        int insertErrors = 0;
        try {
            insertStatement.executeBatch();
        } catch (SQLException e) {
            connection.rollback();
            insertErrors++;
            throw new SQLException("Batch execution failed: " + e.getMessage());
        }
        return insertErrors;
    }

    public static boolean processRow(String[] tradeData, PreparedStatement insertStatement, Connection connection, FileWriter errorLog) throws IOException {
        int retryCount = 0;
        while (retryCount < MAX_RETRY_ATTEMPTS) {
            try {
                validateTradeData(tradeData);
                int trade_id = Integer.parseInt(tradeData[0]);
                String trade_identifier = tradeData[1];
                String ticker_symbol = "";
                if (TickerValidator.isValidTickerSymbol(tradeData[2])) {
                    ticker_symbol = tradeData[2];
                } else {
                    errorLog.write("Invalid ticker symbol" + tradeData[2]);
                    retryCount++;
                    if (retryCount >= MAX_RETRY_ATTEMPTS) {
                        break;
                    }
                    continue;
                }
                int quantity = Integer.parseInt(tradeData[3]);
                double price = Double.parseDouble(tradeData[4]);
                String trade_date = tradeData[5];
                insertStatement.setInt(1, trade_id);
                insertStatement.setString(2, trade_identifier);
                insertStatement.setString(3, ticker_symbol);
                insertStatement.setInt(4, quantity);
                insertStatement.setDouble(5, price);
                insertStatement.setString(6, trade_date);
                insertStatement.addBatch();
                return true;
            } catch (Exception e) {
                retryCount++;
                errorLog.write("Error processing row: " + Arrays.toString(tradeData) + " \n Reason: " + e.getMessage() + "\n");
                if (retryCount >= MAX_RETRY_ATTEMPTS) {
                    return false;
                }
            }
        }
        return false;
    }
}
