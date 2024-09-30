package multithreading.trading_multithreading.config;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;

// Configure HikariCP connection pool
public class HikariCPConfig {
    private static HikariDataSource dataSource;

    private HikariCPConfig() {
        // private constructor to prevent instantiation
    }

    public static HikariDataSource getDataSource() { //static{
        // Configure the HikariCP connection pool
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3307/jdbc"); //bootcamp
        config.setUsername("root");
        config.setPassword("password123");
        // Optional HikariCP settings
        config.setMaximumPoolSize(10); // Max 10 connections in the pool
        config.setMinimumIdle(5); // Minimum idle connections
        config.setConnectionTimeout(30000); // 30 seconds timeout for obtaining a connection - in milliseconds
        config.setIdleTimeout(600000); // 10 minutes idle timeout before connection is closed
        // Create the HikariCP data source
        dataSource = new HikariDataSource(config);

        return dataSource;
    }

    public static Connection getConnection() throws Exception {
        // Fetch a connection from the pool
        return dataSource.getConnection();
    }

    public static void close() {
        // Close the data source (usually when the app shuts down)
        if (dataSource != null) {
            dataSource.close();
        }
    }
}