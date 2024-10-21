package exception_handling.trading;
public class RawCopy {
}

/**
 package exception_handling.trading;

 import exception_handling.trading.exception.HitInsertErrorsThresholdException;
 import exception_handling.trading.exception.InvalidThresholdValueException;
 import exception_handling.trading.util.SecuritiesReaderProcessor;
 import exception_handling.trading.util.TradeFileReader;

 import java.io.*;
 import java.sql.*;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Properties;
 import java.util.Scanner;

 public class org.pavani.Main {
 public static String filePath = "/Users/akm/pavani/JavaBootcamp/trades_sample_1000.csv";
 public static String securitiesFilePath = "/Users/akm/pavani/JavaBootcamp/securitiesReference.csv";
 public static double errorThreshold = 0.25;
 //    static double errorThreshold;
 public static final int batchSize = 100; // insert after 100 rows
 public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
 public static final int MAX_RETRY_ATTEMPTS = 3; // Retry insertion up to 3 times
 public static final String ERROR_LOG_PATH = "error_log.txt"; // Path for logging errors


 public static void validateTradeData(String[] tradeData) throws ParseException {
 if(tradeData.length!=6){
 throw new IllegalArgumentException("Incorrect number of fields");
 }
 // Validate trade_id, quantity (integer), price (double), and trade_date (proper format)
 Integer.parseInt(tradeData[0]); // trade_id
 Integer.parseInt(tradeData[3]); // quantity
 Double.parseDouble(tradeData[4]); // price
 DATE_FORMAT.parse(tradeData[5]); // trade_date
 }

 public static void main(String[] args) {
 try{
 loadFromConfigProperties();
 loadFileFromUserInput();
 loadThresholdFromUserInput();

 SecuritiesReaderProcessor securitiesReaderProcessor = new SecuritiesReaderProcessor();
 securitiesReaderProcessor.readSecurities(securitiesFilePath);

 TradeFileReader tradeFileReaderAndProcessor = new TradeFileReader();
 tradeFileReaderAndProcessor.readTradeDataFromCSV(filePath);

 } catch (Exception e) {
 throw new RuntimeException(e);
 } //                if(args.length>0){ errorThreshold = parseThreshold(args[0]);}
 }

 public static void loadFromConfigProperties() {
 Properties properties = new Properties();
 try (InputStream input = new FileInputStream("trade.properties")) {
 properties.load(input);
 filePath = properties.getProperty("file.path");
 try {
 String errorThresholdString = properties.getProperty("error.threshold");
 errorThreshold = Double.parseDouble(errorThresholdString);
 } catch (NumberFormatException e) {
 //                throw new InvalidThresholdValueException("Invalid threshold in properties file.\"+\n" + " Fix the application.properties file before re-running the program ");
 System.out.println("Invalid threshold in properties file.\"+\n" + " Fix the application.properties file before re-running the program ");
 System.exit(1);
 }
 if (errorThreshold < 1 || errorThreshold > 100) {
 throw new InvalidThresholdValueException("Threshold must be between 1 and 100.");
 }
 } catch (FileNotFoundException e) {
 throw new RuntimeException(e);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 private static void loadFileFromUserInput(){
 Scanner scanner = new Scanner(System.in);
 while(true){
 System.out.print("Enter file path: "+ "(press Enter to use default file or type 'x' to exit): ");
 String inputfilePath = scanner.nextLine();
 if(inputfilePath.isEmpty()){
 break;
 }
 if(inputfilePath.trim().equalsIgnoreCase("x")){
 System.out.println("Exiting the program...");
 System.exit(0);
 }
 if(inputfilePath.contains(".csv")){
 filePath = inputfilePath;
 break;
 } else {
 System.out.println("Invalid file path. Provide a valid csv file");
 }
 }
 }

 private static void loadThresholdFromUserInput(){
 try {
 Scanner scanner = new Scanner(System.in);
 while(true){
 System.out.print("Enter error threshold (1 to 100): "+"press Enter to use default value 25 from properties file:");
 String inputErrorThreshold = scanner.nextLine().trim();
 if(inputErrorThreshold.isEmpty()){
 break;
 }
 if(inputErrorThreshold.trim().equalsIgnoreCase("x")){
 System.out.println("Exiting the program...");
 System.exit(0);
 } else{
 try{
 errorThreshold = Double.parseDouble(inputErrorThreshold);
 } catch (NumberFormatException e) {
 System.out.println("Invalid threshold value. Please provide a valid number between 1 and 100.");
 continue;
 }
 }
 if(errorThreshold<1 || errorThreshold>100){
 System.out.println("Threshold must be between 1 and 100.");
 continue;
 } else {
 break;
 }
 }
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 //
 //    private static double parseThreshold(String input) {
 //        try{
 //            double value = Double.parseDouble(input);
 ////            int value = Integer.parseInt(input);
 ////            int percent = (int) (value*100);
 ////            System.out.println(percent);
 //            if(value<1 || value>100){
 //                throw new InvalidThresholdValueException("Threshold must be between 1 and 100.");
 //            }
 //            return value;
 //        } catch(NumberFormatException e){
 //            throw new InvalidThresholdValueException("Invalid input. Please enter a valid threshold value between 1 and 100.");
 //        }
 //    }

 public static int executeBatch(PreparedStatement insertStatement, Connection connection) throws SQLException, HitInsertErrorsThresholdException {
 try {
 int[] results = insertStatement.executeBatch();
 int batchInsertErrors = 0;
 // Count unsuccessful inserts
 for (int result : results) {
 if (result == Statement.EXECUTE_FAILED) {
 batchInsertErrors++;
 System.err.println("Insert failed for batch index: " + result);
 }
 }
 if (batchInsertErrors >0) {
 throw new HitInsertErrorsThresholdException("Batch insert failed for "+batchInsertErrors+" rows.");
 }
 return batchInsertErrors; // Return number of insert errors
 } catch (SQLException e) {
 connection.rollback(); // Rollback in case of failure
 throw new SQLException("Batch execution failed. Transaction rolled back: " + e.getMessage());
 }
 }
 }







 package exception_handling.trading.service;

 import exception_handling.trading.util.TickerValidator;

 import java.io.FileWriter;
 import java.io.IOException;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.SQLException;
 import java.util.Arrays;

 import static exception_handling.trading.org.pavani.Main.MAX_RETRY_ATTEMPTS;
 import static exception_handling.trading.org.pavani.Main.validateTradeData;

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
 while (retryCount < MAX_RETRY_ATTEMPTS) { //!rowInserted &&
 try {
 validateTradeData(tradeData);
 int trade_id = Integer.parseInt(tradeData[0]);
 String trade_identifier = tradeData[1];
 String ticker_symbol = "";
 if (TickerValidator.isValidTickerSymbol(tradeData[2])) {
 ticker_symbol = tradeData[2];
 //                                System.out.println(ticker_symbol);
 } else {
 errorLog.write("Invalid ticker symbol" + tradeData[2]);
 //                    errorCount++;
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
 //                successfulInserts++;
 //                rowInserted = true;
 return true;
 } catch (Exception e) {
 retryCount++;
 //                errorLog.write("Error processing row: "+line+" \n Reason: "+e.getMessage()+"\n");
 errorLog.write("Error processing row: " + Arrays.toString(tradeData) + " \n Reason: " + e.getMessage() + "\n");
 //                System.out.println("Error processing row: (attempt "+retryCount+"): "+e.getMessage());
 if (retryCount >= MAX_RETRY_ATTEMPTS) {
 //                    errorCount++;
 //                    System.out.println("Max retry attempts reached for row: "+line);
 return false;
 }
 }
 }
 return false;
 }

 }







 package exception_handling.trading.util;

 import exception_handling.trading.config.DbConnection;
 import exception_handling.trading.service.SecuritiesProcessor;
 import java.io.BufferedReader;
 import java.io.FileReader;
 import java.io.IOException;
 import java.sql.Connection;
 import java.sql.SQLException;
 import static exception_handling.trading.org.pavani.Main.*;

 public class SecuritiesReader {
 public void readSecurities(String file) {
 try (Connection connection = DbConnection.getConnection()) {
 connection.setAutoCommit(false); //start transaction

 try (BufferedReader fileReader = new BufferedReader(new FileReader(securitiesFilePath))) {
 SecuritiesProcessor.processSecurities(connection, fileReader);
 connection.commit();
 System.out.println("Securities data inserted successfully");
 } catch (SQLException e) {
 throw new RuntimeException(e);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 } catch (SQLException e) {
 throw new RuntimeException(e);
 }
 }
 }







 package exception_handling.trading.service;

 import java.io.BufferedReader;
 import java.io.IOException;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.SQLException;

 import static exception_handling.trading.org.pavani.Main.executeBatch;

 public class SecuritiesProcessorService {
 public static void processSecurities(Connection connection, BufferedReader fileReader) throws SQLException {
 String insertSQL = "INSERT INTO SecuritiesReference (symbol, description) VALUES (?,?)";
 try(PreparedStatement insertStatement = connection.prepareStatement(insertSQL)){
 String line;
 // Skip the header if present
 if ((line = fileReader.readLine()) != null && line.startsWith("symbol")) {
 //                continue;
 }
 while ((line = fileReader.readLine()) != null) {
 // Split the CSV line into trade data fields
 String[] data = line.split(",");
 if (data.length < 2) {
 System.out.println("Invalid row: " + line);
 continue; // Skip invalid rows
 }
 try {
 insertStatement.setString(1, data[0]);
 insertStatement.setString(2, data[1]);
 insertStatement.addBatch();
 } catch (Exception e) {
 System.out.println("Error processing row: " + e.getMessage());
 }
 }
 executeBatch(insertStatement, connection);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }
 }







 package exception_handling.trading.util;

 import exception_handling.trading.config.DbConnection;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;

 public class TickerValidator {
 public static boolean isValidTickerSymbol(String symbol) throws SQLException {
 try (Connection connection = DbConnection.getConnection()) {
 //            connection.setAutoCommit(false); //start transaction
 String selectSQL = "SELECT symbol FROM SecuritiesReference where symbol = ?";
 try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
 selectStatement.setString(1,symbol);
 ResultSet rs = selectStatement.executeQuery();
 if (rs.next()) {
 return true;
 } else {
 return false;
 }
 }
 }
 }
 }






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
 import static exception_handling.trading.org.pavani.Main.*;

 public class TradeFileReader {
 public void readTradeDataFromCSV(String filePath) throws HitErrorsThresholdException {
 try (Connection connection = DbConnection.getConnection()) {
 connection.setAutoCommit(false); //start transaction

 try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
 FileWriter errorLog = new FileWriter(ERROR_LOG_PATH, true)) {

 tradeFileWriter(fileReader, connection, errorLog);
 connection.commit();
 System.out.println("Trade data has been inserted successfully.");

 //                    System.out.println("Error count:"+errorCount);
 //                    System.out.println("total rows:"+totalRows);
 //                    System.out.println("perc: -- "+(((double) errorCount/totalRows))*100);
 //                    System.out.println("Error threshold: "+errorThreshold);
 //                    System.out.println("TEST---"+ ((double) errorCount/totalRows >errorThreshold));

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
 //                        insertErrors += executeBatch(insertStatement, connection, successfulInserts);
 //                        insertStatement.executeBatch();
 }
 }
 // Execute remaining batch
 if(successfulInserts % batchSize!=0){
 //                    insertStatement.executeBatch();
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











 **/











