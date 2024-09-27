package exception_handling.trading.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static exception_handling.trading.Main.executeBatch;

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
