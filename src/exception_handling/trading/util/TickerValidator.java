package exception_handling.trading.util;

import exception_handling.trading.config.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TickerValidator {
    public static boolean isValidTickerSymbol(String symbol) throws SQLException {
        try (Connection connection = DbConnection.getConnection()) {
            String selectSQL = "SELECT symbol FROM SecuritiesReference where symbol = ?";
//            String selectSQL = "SELECT 1 FROM SecuritiesReference where symbol = ?";
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
