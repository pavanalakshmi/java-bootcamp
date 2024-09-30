package multithreading.trading_multithreading.service;

// reads queues trade_id and run threads in parallel
// read payload from trade_payloads table
// looks up symbol in securities table; if available -> add
// acc no, CUSIP, direction, quantity, posted_status

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.interfaces.TradeProcessorInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TradeProcessor implements TradeProcessorInterface {
    private ExecutorService executor;
    HikariDataSource dataSource;
    Position position;

    public TradeProcessor() {
        executor = Executors.newFixedThreadPool(3);
        dataSource = HikariCPConfig.getDataSource();
        position = new Position();
    }

    public void processTrade(Map<String , LinkedBlockingQueue<String>> tradeDistributionQueuesMap){
        try {
            for(Map.Entry<String , LinkedBlockingQueue<String>> entry: tradeDistributionQueuesMap.entrySet()){
                LinkedBlockingQueue<String> queue = entry.getValue();
                executor.submit(() -> {
                    try {
                        processQueue(queue);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
                executor.shutdown();
                try {
                    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                        executor.shutdownNow();  // Forcibly terminate if needed
                    }
                } catch (InterruptedException e) {
                    executor.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
        }

    private void processQueue(LinkedBlockingQueue<String> queue) throws SQLException {
//        for(String tradeId: queue){ readPayload(tradeId); }
        String tradeId;
        while ((tradeId = queue.poll()) != null) {
            readPayload(tradeId);
        }
    }

    public void readPayload(String tradeId) throws SQLException {
        String account_number = "";
        String CUSIP = "";
        String direction = "";
        int quantity = 0;

        String selectSQL = "SELECT payload FROM trade_payloads where trade_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
            selectStatement.setString(1, tradeId);
            ResultSet payload = selectStatement.executeQuery();
            while (payload.next()) {
                String payloadString = payload.getString("payload");
                String[] payloadData = payloadString.split(",");
                account_number = payloadData[2];
                CUSIP = payloadData[3];
                direction = payloadData[4];
                quantity = Integer.parseInt(payloadData[5]);
//                account_number = payload.getString("account_number");
            }
        } catch (SQLException e) {
            System.out.println("Error processing row: " + e.getMessage());
            throw new RuntimeException(e);
        }
        if(isValidCUSIPSymbol(CUSIP)){
            insertToJournalEntry(account_number, CUSIP, direction, quantity);
            position.upsertPositions(account_number, CUSIP, direction, quantity);
        }
    }

    private void insertToJournalEntry(String account_number, String CUSIP, String direction, int quantity) {
        String insertSQL = "INSERT INTO journal_entry VALUES (?,?,?,?,?)"; //total 9029
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)){
            insertStatement.setString(1, account_number);
            insertStatement.setString(2, CUSIP);
            insertStatement.setString(3, direction);
            insertStatement.setInt(4, quantity);
            insertStatement.setString(5, "true");
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidCUSIPSymbol(String cusip) {
        String selectSQL = "SELECT symbol FROM SecuritiesReference where symbol = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSQL)){
            selectStatement.setString(1,cusip);
            ResultSet rs = selectStatement.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}