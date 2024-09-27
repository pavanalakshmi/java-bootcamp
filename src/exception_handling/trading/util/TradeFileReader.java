package exception_handling.trading.util;

import exception_handling.trading.config.DbConnection;
import exception_handling.trading.exception.HitErrorsThresholdException;
import exception_handling.trading.exception.HitInsertErrorsThresholdException;
import exception_handling.trading.service.TradeProcessorService;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static exception_handling.trading.Main.*;

public class TradeFileReader {
    public void readTradeDataFromCSV(String filePath) throws HitErrorsThresholdException {
        try (Connection connection = DbConnection.getConnection()) {
            connection.setAutoCommit(false); //start transaction
            try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
                 FileWriter errorLog = new FileWriter(ERROR_LOG_PATH, true)) {
                readFile(fileReader, connection, errorLog);
                connection.commit();
                System.out.println("Trade data has been inserted successfully.");
            } catch (IOException e) {
                throw new RuntimeException("File read error: "+e.getMessage());
            }
            catch (SQLException e){
                connection.rollback(); // Rollback the transaction if batch execution fails
                throw new HitInsertErrorsThresholdException("Error during batch insertion: "+e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error: "+e.getMessage());
        }
    }

    public void readFile(BufferedReader fileReader, Connection connection, FileWriter errorLog) {
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
                boolean rowInserted = TradeProcessorService.processRow(tradeData, insertStatement, connection, errorLog);
                if (rowInserted) {
                    successfulInserts++;
                } else {
                    errorCount++;
                }
                // Check if more than the error threshold of rows contain errors
                checkErrorThreshold(errorCount, totalRows);
                // Execute batch after reaching batch size
                if(successfulInserts % batchSize==0){
                    insertErrors += TradeProcessorService.dbWriter(insertStatement, connection);
                }
            }
            // Execute remaining batch
            if(successfulInserts % batchSize!=0){
                insertErrors += TradeProcessorService.dbWriter(insertStatement, connection);
            }
            // Log results
            logSummary(totalRows, successfulInserts, insertErrors, errorCount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkErrorThreshold(int errorCount, int totalRows) {
        // Check if more than 25% of the rows in the file contain errors (e.g., missing data, incorrect data types)
        if(((double) errorCount/totalRows)*100 >errorThreshold){
            throw new HitErrorsThresholdException("More than 25% rows contain errors. Aborting batch process");
        }
    }

    public void logSummary(int totalRows, int successfulInserts, int insertErrors, int errorCount) {
        System.out.println("Total rows: " + totalRows);
        System.out.println("Successful inserts: " + successfulInserts);
        System.out.println("Failed inserts: " + insertErrors);
        System.out.println("Parsing errors: " + errorCount);
    }
}
