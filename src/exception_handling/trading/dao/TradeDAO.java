package exception_handling.trading.dao;

import exception_handling.trading.interfaces.TradeDBWriter;
import exception_handling.trading.interfaces.TradeFileWriter;
import exception_handling.trading.service.TradeProcessorService;
import exception_handling.trading.util.TradeFileReader;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static exception_handling.trading.Main.batchSize;

public class TradeDAO implements TradeDBWriter, TradeFileWriter {
    private TradeFileReader tradeFileReader;
    private TradeProcessorService tradeProcessorService;

    public TradeDAO() {
        tradeFileReader = new TradeFileReader();
        tradeProcessorService = new TradeProcessorService();
    }

    public int dbWriter(PreparedStatement insertStatement, Connection connection) throws SQLException {
        int insertErrors = 0;
        try {
            insertStatement.executeBatch();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Batch execution failed: " + e.getMessage());
        }
        return insertErrors;
    }

    public void tradeFileWriter(BufferedReader fileReader, Connection connection, FileWriter errorLog) {
        String line;
        int totalRows = 0;
        int errorCount = 0;
        int successfulInserts = 0;
        int insertErrors = 0;
        String insertSQL = "INSERT INTO Trades (trade_id, trade_identifier, ticker_symbol, quantity, price, trade_date) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
            while ((line = fileReader.readLine()) != null) {
                totalRows++;
                // Skip the header if present
                if (line.startsWith("trade_id")) {
                    continue;
                }
                // Split the CSV line into trade data fields
                String[] tradeData = line.split(",");
                boolean rowInserted = tradeProcessorService.processTradeRow(tradeData, insertStatement, connection, errorLog);
                if (rowInserted) {
                    successfulInserts++;
                } else {
                    errorCount++;
                }
                // Check if more than the error threshold of rows contain errors
                tradeFileReader.checkErrorThreshold(errorCount, totalRows);
                // Execute batch after reaching batch size
                if(successfulInserts % batchSize==0){
                    insertErrors += dbWriter(insertStatement, connection);
                }
            }
            // Execute remaining batch
            if(successfulInserts % batchSize!=0){
                insertErrors += dbWriter(insertStatement, connection);
            }
            // Log results
            tradeFileReader.logSummary(totalRows, successfulInserts, insertErrors, errorCount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
