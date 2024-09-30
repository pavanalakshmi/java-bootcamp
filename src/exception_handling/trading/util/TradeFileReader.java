package exception_handling.trading.util;

import exception_handling.trading.config.DbConnection;
import exception_handling.trading.dao.TradeDAO;
import exception_handling.trading.exception.HitErrorsThresholdException;
import exception_handling.trading.exception.HitInsertErrorsThresholdException;
import exception_handling.trading.interfaces.TradeFileReaderInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import static exception_handling.trading.Main.*;

public class TradeFileReader implements TradeFileReaderInterface {

    TradeDAO tradeDAO;
    public TradeFileReader() {
        tradeDAO = new TradeDAO();
    }

    public void readTradeDataFromCSV(String filePath) throws HitErrorsThresholdException {
        try (Connection connection = DbConnection.getConnection()) {
            connection.setAutoCommit(false); //start transaction
            try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
                 FileWriter errorLog = new FileWriter(ERROR_LOG_PATH, true)) {
                tradeDAO.tradeFileWriter(fileReader, connection, errorLog);
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
