package org.pavani.multithreading.trading_multithreading.util;

import com.zaxxer.hikari.HikariDataSource;
import org.pavani.multithreading.trading_multithreading.exception.HikariCPConnectionException;
import org.pavani.multithreading.trading_multithreading.exception.TransactionHandlingException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;

public class JDBCTransactionUtil implements TransactionUtil{

    private HikariDataSource dataSource;
    private static final ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getInstance();
    private static final String DB_USERNAME = applicationConfigProperties.getDbUserName();
    private static final String DB_PASSWORD = applicationConfigProperties.getDbPasswords();
    private static final String DB_URL = applicationConfigProperties.getDbUrl();
    private static JDBCTransactionUtil instance;
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    Logger logger = Logger.getLogger(JDBCTransactionUtil.class.getName());

    private JDBCTransactionUtil() {
        // private constructor to prevent instantiation
    }

    public static synchronized JDBCTransactionUtil getInstance() {
        if (instance == null) {
            instance = new JDBCTransactionUtil();
        }
        return instance;
    }

    public void createDataSource() {
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl(DB_URL); //bootcamp
        config.setUsername(DB_USERNAME);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(40000);
        config.setIdleTimeout(600000);
        dataSource = new HikariDataSource(config);
    }

    private HikariDataSource getHikariDataSource() {
        if (dataSource == null) {
            createDataSource();
        }
        return dataSource;
    }

    public Connection getConnection() {
        Connection connection = connectionHolder.get();
        if (connection == null) {
            dataSource = getHikariDataSource();
            try {
                connection = dataSource.getConnection();
                connectionHolder.set(connection);
            } catch (Exception e) {
                throw new HikariCPConnectionException("Error getting connection from HikariCP" + e.getMessage());
            }
        }
        return connection;
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Override
    public void startTransaction() {
        try {
            getConnection().setAutoCommit(false);
        } catch (Exception e) {
            logger.warning(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void commitTransaction() {
        try {
            connectionHolder.get().commit();
            connectionHolder.get().setAutoCommit(false);
            closeConnection();
        } catch (SQLException e) {
            throw new TransactionHandlingException("error committing transaction" + e);
        }
    }

    private void closeConnection() {
        Connection connection = connectionHolder.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                logger.warning(Arrays.toString(e.getStackTrace()));
            } finally {
                connectionHolder.remove();
            }
        }
    }

    @Override
    public void rollbackTransaction() {
        try {
            connectionHolder.get().rollback();
            connectionHolder.get().setAutoCommit(false);
            closeConnection();
        } catch (SQLException e) {
            throw new TransactionHandlingException("error rolling back transaction" + e);
        }
    }

    }

