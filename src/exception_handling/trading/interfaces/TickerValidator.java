package exception_handling.trading.interfaces;

import java.sql.SQLException;

public interface TickerValidator {
    boolean isValidTickerSymbol(String symbol) throws SQLException;
}
