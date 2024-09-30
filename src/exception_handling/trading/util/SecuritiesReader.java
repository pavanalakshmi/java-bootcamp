package exception_handling.trading.util;

import exception_handling.trading.config.DbConnection;
import exception_handling.trading.interfaces.ReadSecurities;
import exception_handling.trading.service.SecuritiesProcessorService;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import static exception_handling.trading.Main.*;

public class SecuritiesReader implements ReadSecurities {
    SecuritiesProcessorService securitiesProcessorService;
    public SecuritiesReader() {
        securitiesProcessorService = new SecuritiesProcessorService();
    }

    public void readSecurities() {
        try (Connection connection = DbConnection.getConnection()) {
            connection.setAutoCommit(false); //start transaction
            try (BufferedReader fileReader = new BufferedReader(new FileReader(securitiesFilePath))) {
                securitiesProcessorService.processSecurities(connection, fileReader);
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