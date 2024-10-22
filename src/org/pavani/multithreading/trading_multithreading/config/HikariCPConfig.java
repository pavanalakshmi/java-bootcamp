package org.pavani.multithreading.trading_multithreading.config;

import com.zaxxer.hikari.HikariDataSource;
import org.pavani.multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.sql.Connection;

public class HikariCPConfig {
    private static HikariDataSource dataSource;
    private static final ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getInstance();
    private static final String DB_USERNAME = applicationConfigProperties.getDbUserName();
    private static final String DB_PASSWORD = applicationConfigProperties.getDbPasswords();
    private static final String DB_URL = applicationConfigProperties.getDbUrl();

    private HikariCPConfig() {
        // private constructor to prevent instantiation
    }

    public static HikariDataSource getDataSource() {
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl(DB_URL); //bootcamp
        config.setUsername(DB_USERNAME);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(40000);
        config.setIdleTimeout(600000);
        dataSource = new HikariDataSource(config);
        return dataSource;
    }

    public static Connection getConnection() throws Exception {
        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}