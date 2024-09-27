package exception_handling.trading;

import exception_handling.trading.exception.HitInsertErrorsThresholdException;
import exception_handling.trading.exception.InvalidThresholdValueException;
import exception_handling.trading.util.SecuritiesReader;
import exception_handling.trading.util.TradeFileReader;
import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static String filePath = "/Users/akm/pavani/JavaBootcamp/trades_sample_1000.csv";
    public static String securitiesFilePath = "/Users/akm/pavani/JavaBootcamp/securitiesReference.csv";
    public static double errorThreshold = 0.25;
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
            SecuritiesReader securitiesReaderProcessor = new SecuritiesReader();
            securitiesReaderProcessor.readSecurities();
            TradeFileReader tradeFileReaderAndProcessor = new TradeFileReader();
            tradeFileReaderAndProcessor.readTradeDataFromCSV(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
                System.out.println("Invalid threshold in properties file.\"+\n" + " Fix the application.properties file before re-running the program ");
                System.exit(1);
            }
            if (errorThreshold < 1 || errorThreshold > 100) {
                throw new InvalidThresholdValueException("Threshold must be between 1 and 100.");
            }
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