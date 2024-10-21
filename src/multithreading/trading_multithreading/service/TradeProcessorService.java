package multithreading.trading_multithreading.service;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.config.RabbitMQConfig;
import multithreading.trading_multithreading.dao.JournalEntryDAO;
import multithreading.trading_multithreading.dao.ReadPayloadDAO;
import multithreading.trading_multithreading.factory.BeanFactory;
import multithreading.trading_multithreading.model.Trade;
import multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.concurrent.*;

public class TradeProcessorService implements TradeProcessor {
    private final ExecutorService executor;
    HikariDataSource dataSource;
    Position position;
    ReadPayloadDAO readPayloadDAO;
    private static JournalEntryDAO journalEntryDAO;
    private static ApplicationConfigProperties applicationConfigProperties;
    Map<String, LinkedBlockingQueue<String>> map;
    int queueCount;
    int maxRetryCount;
    private final Map<String, Integer> retryMap;
    private final LinkedBlockingQueue<String> deadLetterQueue;
    ConnectionFactory factory;
    RabbitMQConfig rabbitMQConfig;
    private final static String EXCHANGE_NAME = "trade_MQ";

    public TradeProcessorService(Map<String, LinkedBlockingQueue<String>> queuesMap) {
        dataSource = HikariCPConfig.getDataSource();
        position = new Position();
        readPayloadDAO = new ReadPayloadDAO();
        applicationConfigProperties = ApplicationConfigProperties.getInstance();
        queueCount = applicationConfigProperties.getTradeProcessorQueueCount();
        map = queuesMap;
        executor = Executors.newFixedThreadPool(applicationConfigProperties.getTradeProcessorThreadPoolSize());
        maxRetryCount = applicationConfigProperties.getMaxRetryAttempts();
        retryMap = new ConcurrentHashMap<>();
        deadLetterQueue = new LinkedBlockingQueue<>();
        rabbitMQConfig = new RabbitMQConfig();
        factory = rabbitMQConfig.connect();
        journalEntryDAO = BeanFactory.getJournalEntryDAO();
    }

    public void processTrade() {
        if (applicationConfigProperties.getUseRabbitMQ()) {
            processRabbitMQLogic();
        } else {
            for (Map.Entry<String, LinkedBlockingQueue<String>> entry : map.entrySet()) {
                LinkedBlockingQueue<String> queue = entry.getValue();
                executor.submit(() -> {
                    try {
                        if (applicationConfigProperties.getUseStoredProcedure()){
                            processTradeQueue(queue);
                        } else{
                            processTradeQueueWithoutStoredProcedure(queue);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            executor.shutdown();
            try {
                if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    public void processRabbitMQLogic() {
        ExecutorService executorService = Executors.newFixedThreadPool(applicationConfigProperties.getTradeProcessorThreadPoolSize());
        // Submit the consumer task
        Future<Void> consumerFuture = null;
        for (int i = 0; i <= queueCount; i++) {
            RabbitMQConsumerCallable consumerTask = new RabbitMQConsumerCallable("trading_queue_"+i);
            consumerFuture = executorService.submit(consumerTask);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown signal received. Stopping consumer...");
            executorService.shutdownNow();
            try {
                if (!executorService.isTerminated()) {
                    executorService.awaitTermination(5, TimeUnit.SECONDS);
                }
            } catch (InterruptedException e) {
                System.out.println("Shutdown interrupted.");
                Thread.currentThread().interrupt();
            }
            System.out.println("Consumer stopped.");
        }));

        try {
            consumerFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            System.out.println("Exception while waiting for consumer to finish: " + e.getMessage());
        }

    }

    private void processTradeQueue(LinkedBlockingQueue<String> queue) throws InterruptedException {
        String tradeId;
        int emptyPollCount = 0;
        int maxEmptyPolls = 3;
        while (emptyPollCount < maxEmptyPolls) {
            tradeId = queue.poll(500, TimeUnit.MILLISECONDS);
            if (tradeId == null) {
                emptyPollCount++;
            } else{
                emptyPollCount = 0;
                if (tradeId.equals("END")) {
                    break;
                }
                executeTrade(tradeId);
            }
        }
    }

    public void executeTrade(String tradeId) {
        try (Connection connection = dataSource.getConnection();
             CallableStatement callableStatement = connection.prepareCall("{CALL INSERT_JOURNAL_UPDATE_POSITION(?, ?, ?, ?, ?, ?)}")) {
            String accountNumber;
            String payload = readPayloadDAO.readPayload(tradeId);
            String[] payloadData = payload.split(",");

            Trade trade = new Trade(payloadData[2], payloadData[3], payloadData[4], Integer.parseInt(payloadData[5]), tradeId);
            accountNumber = trade.accountNumber();
            String cusip = trade.cusip();
            String direction = trade.direction();
            int quantity = trade.quantity();

            if (readPayloadDAO.isValidCUSIPSymbol(cusip)) {
                callableStatement.setString(1, accountNumber);
                callableStatement.setString(2, cusip);
                callableStatement.setString(3, direction);
                callableStatement.setInt(4, quantity);
                callableStatement.setString(5, tradeId);
                callableStatement.registerOutParameter(6, Types.VARCHAR);
                callableStatement.execute();
                String statusCode = callableStatement.getString(6);
                System.out.println("Stored procedure result: " + statusCode);

                switch (statusCode) {
                    case "POSITION_UPDATE_DONE":
                        System.out.println("Trade successfully processed: " + tradeId);
                        break;
                    case "POSITION_INSERT_FAILED":
                        System.out.println("Trade Position Insert Failed : " + tradeId);
                        break;
                    case "JE_INSERT_FAILED", "POSITION_UPDATE_FAILED_OPTIMISTIC_LOCKING":
                        retryOrDeadLetterQueue(statusCode, accountNumber, tradeId);
                        break;
                    default:
                        throw new RuntimeException("Unexpected status from SP: " + statusCode);
                }
            } else {
                System.out.println("Invalid CUSIP: " + cusip);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void retryOrDeadLetterQueue(String statusCode, String accountNumber, String tradeId) {
        int retryCount = retryMap.getOrDefault(accountNumber, 0);
        if (retryCount < maxRetryCount) {
            retryCount++;
            retryMap.put(accountNumber, retryCount);
            System.out.println("Retrying trade: " + tradeId + " due to " + statusCode);
        } else {
            System.err.println("Max retries reached for trade: " + tradeId + ", sending to DLQ");
            if (!deadLetterQueue.offer(tradeId)) {
                System.err.println("Failed to add trade to DLQ: " + tradeId);
            }
        }
    }

    private void processTradeQueueWithoutStoredProcedure(LinkedBlockingQueue<String> queue) throws InterruptedException, SQLException {
        String tradeId;
        String cusip;
        while ((tradeId = queue.poll(2, TimeUnit.SECONDS)) != null) {
            if (tradeId.equals("END")) {
                return;
            }
            String payload = readPayloadDAO.readPayload(tradeId);
            String[] payloadData = payload.split(",");
            Trade trade = new Trade(payloadData[2], payloadData[3], payloadData[4], Integer.parseInt(payloadData[5]), tradeId);
            cusip = trade.cusip();
            if (readPayloadDAO.isValidCUSIPSymbol(cusip)) {
                journalEntryDAO.insertToJournalEntry(trade);
                position.upsertPositions(trade);
            }
        }
    }

    public class RabbitMQConsumerCallable implements Callable<Void> {
        private final String queueName;
        public RabbitMQConsumerCallable(String queueName) {
            this.queueName = queueName;
        }
        @Override
        public Void call() throws Exception {
            try (com.rabbitmq.client.Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.exchangeDeclare(EXCHANGE_NAME, "direct");
                channel.queueDeclare(queueName, true, false, false, null);
                channel.queueBind(queueName, EXCHANGE_NAME, queueName);
                System.out.println(" [*] Waiting for messages in '" + queueName + "'.");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                    String tradeId = message.trim();
                    executeTrade(tradeId);
                };
                CancelCallback cancelCallback = consumerTag -> {
                };
                channel.basicConsume(queueName, true, deliverCallback, cancelCallback);

                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(1000); // Keep checking every second
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer interrupted, shutting down...");
            }
            return null;
        }
    }
}