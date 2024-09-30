package exception_handling.trading.service;

import exception_handling.trading.interfaces.ProcessTradeRow;
import exception_handling.trading.util.TickerValidator;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import static exception_handling.trading.Main.MAX_RETRY_ATTEMPTS;
import static exception_handling.trading.Main.validateTradeData;

public class TradeProcessorService implements ProcessTradeRow {
    TickerValidator tickerValidator;
    public TradeProcessorService() {
        tickerValidator = new TickerValidator();
    }

    public boolean processTradeRow(String[] tradeData, PreparedStatement insertStatement, Connection connection, FileWriter errorLog) throws IOException {
        int retryCount = 0;
        while (retryCount < MAX_RETRY_ATTEMPTS) {
            try {
                validateTradeData(tradeData);
                int tradeId = Integer.parseInt(tradeData[0]);
                String tradeIdentifier = tradeData[1];
                String tickerSymbol;
                if (tickerValidator.isValidTickerSymbol(tradeData[2])) {
                    tickerSymbol = tradeData[2];
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
                String tradeDate = tradeData[5];
                insertStatement.setInt(1, tradeId);
                insertStatement.setString(2, tradeIdentifier);
                insertStatement.setString(3, tickerSymbol);
                insertStatement.setInt(4, quantity);
                insertStatement.setDouble(5, price);
                insertStatement.setString(6, tradeDate);
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
