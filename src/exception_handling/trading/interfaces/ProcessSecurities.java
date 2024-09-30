package exception_handling.trading.interfaces;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.SQLException;

public interface ProcessSecurities {
    void processSecurities(Connection connection, BufferedReader fileReader) throws SQLException;
    }
