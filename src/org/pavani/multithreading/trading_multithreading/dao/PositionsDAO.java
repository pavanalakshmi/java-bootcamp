package org.pavani.multithreading.trading_multithreading.dao;

import org.pavani.multithreading.trading_multithreading.model.Trade;

import java.sql.Connection;
import java.sql.SQLException;

public interface PositionsDAO {
    void insertToPositions(Trade trade, Connection connection);
    int updatePositions(Trade trade, Connection connection, int version, int newQuantity) throws SQLException;
    }
