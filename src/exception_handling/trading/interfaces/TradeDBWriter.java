package exception_handling.trading.interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface TradeDBWriter {
    int dbWriter(PreparedStatement insertStatement, Connection connection) throws SQLException;
    }
