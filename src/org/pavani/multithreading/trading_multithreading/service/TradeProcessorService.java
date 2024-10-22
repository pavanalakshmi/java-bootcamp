package org.pavani.multithreading.trading_multithreading.service;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.zaxxer.hikari.HikariDataSource;
import org.pavani.multithreading.trading_multithreading.config.HikariCPConfig;
import org.pavani.multithreading.trading_multithreading.config.RabbitMQConfig;
import org.pavani.multithreading.trading_multithreading.dao.JournalEntryDAO;
import org.pavani.multithreading.trading_multithreading.dao.ReadPayloadDAO;
import org.pavani.multithreading.trading_multithreading.factory.BeanFactory;
import org.pavani.multithreading.trading_multithreading.model.Trade;
import org.pavani.multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class TradeProcessorService implements TradeProcessor {
    private final ExecutorService tradeProcessorExecutor;
    private static final JournalEntryDAO journalEntryDAO = BeanFactory.getJournalEntryDAO();
    private static final ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getInstance();
    private final Map<String, Integer> retryMap;
    private final LinkedBlockingQueue<String> deadLetterQueue;
    private static final String EXCHANGE_NAME = "trade_MQ";
    HikariDataSource dataSource;
    Position position;
    ReadPayloadDAO readPayloadDAO;
    Map<String, LinkedBlockingQueue<String>> map;
    ConnectionFactory factory;
    RabbitMQConfig rabbitMQConfig;
    Logger logger = Logger.getLogger(TradeProcessorService.class.getName());
    int queueCount;
    int maxRetryCount;

    public TradeProcessorService(Map<String, LinkedBlockingQueue<String>> queuesMap) {
        dataSource = HikariCPConfig.getDataSource();
        position = new Position();
        readPayloadDAO = new ReadPayloadDAO();
        queueCount = applicationConfigProperties.getTradeProcessorQueueCount();
        map = queuesMap;
        tradeProcessorExecutor = Executors.newFixedThreadPool(applicationConfigProperties.getTradeProcessorThreadPoolSize());
        maxRetryCount = applicationConfigProperties.getMaxRetryAttempts();
        retryMap = new ConcurrentHashMap<>();
        deadLetterQueue = new LinkedBlockingQueue<>();
        rabbitMQConfig = new RabbitMQConfig();
        factory = rabbitMQConfig.connect();
    }

    public void processTrade() {
        if (Boolean.TRUE.equals(applicationConfigProperties.getUseRabbitMQ())) {
            processRabbitMQLogic();
        } else {
            processTradeQueues();
            shutdownExecutor();
        }
    }

    private void shutdownExecutor() {
        tradeProcessorExecutor.shutdown();
        try {
            if (!tradeProcessorExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                tradeProcessorExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            tradeProcessorExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void processTradeQueues() {
        for (Map.Entry<String, LinkedBlockingQueue<String>> entry : map.entrySet()) {
            LinkedBlockingQueue<String> queue = entry.getValue();
            tradeProcessorExecutor.submit(() -> {
                try {
                    if (Boolean.TRUE.equals(applicationConfigProperties.getUseStoredProcedure())){
                        processTradeQueue(queue);
                    } else{
                        processTradeQueueWithoutStoredProcedure(queue);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (SQLException e) {
                    String output = "error while submitting tradeProcessorExecutor service";
                    logger.info(output);
                }
            });
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
            logger.info("Shutdown signal received. Stopping consumer...");
            executorService.shutdownNow();
            try {
                if (!executorService.isTerminated()) {
                    boolean terminated = executorService.awaitTermination(5, TimeUnit.SECONDS);
                    if(!terminated){
                        logger.info("ExecutorService did not terminate within the specified timeout.");
                        executorService.shutdownNow();
                    }
                }
            } catch (InterruptedException e) {
                logger.info("Shutdown interrupted.");
                Thread.currentThread().interrupt();
            }
            logger.info("Consumer stopped.");
        }));

        if(consumerFuture!=null){
            try {
                consumerFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                logger.info("Exception while waiting for consumer to finish: " + e.getMessage());
            }
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
                String storedProcedureMsg = "Stored procedure result: " + statusCode;
                logger.info(storedProcedureMsg);

                switch (statusCode) {
                    case "POSITION_UPDATE_DONE":
                        String successOutput = "Trade successfully processed: " + tradeId;
                        logger.info(successOutput);
                        break;
                    case "POSITION_INSERT_FAILED":
                        String failedOutput = "Trade Position Insert Failed : " + tradeId;
                        logger.info(failedOutput);
                        break;
                    case "JE_INSERT_FAILED", "POSITION_UPDATE_FAILED_OPTIMISTIC_LOCKING":
                        retryOrDeadLetterQueue(statusCode, accountNumber, tradeId);
                        break;
                    default:
                        String defaultMsg = "Unexpected status from SP: " + statusCode;
                        logger.info(defaultMsg);
                }
            } else {
                String printMsg = "Invalid CUSIP: " + cusip;
                logger.info(printMsg);
            }
        } catch (SQLException e) {
            String exceptionMsg = "Exception in connection";
            logger.info(exceptionMsg);
        }
    }

    private void retryOrDeadLetterQueue(String statusCode, String accountNumber, String tradeId) {
        int retryCount = retryMap.getOrDefault(accountNumber, 0);
        if (retryCount < maxRetryCount) {
            retryCount++;
            retryMap.put(accountNumber, retryCount);
            String outputMessage = "Retrying trade: " + tradeId + " due to " + statusCode;
            logger.info(outputMessage);
        } else {
            String outputMsg = "Max retries reached for trade: " + tradeId + ", sending to DLQ";
            logger.info(outputMsg);
            if (!deadLetterQueue.offer(tradeId)) {
                String dlqMsg = "Failed to add trade to DLQ: " + tradeId;
                logger.info(dlqMsg);
            }
        }
    }

    private void processTradeQueueWithoutStoredProcedure(LinkedBlockingQueue<String> queue) throws InterruptedException, SQLException {
        String tradeId;
        while ((tradeId = queue.poll(500, TimeUnit.MILLISECONDS)) != null) {
            if (tradeId.equals("END")) {
                return;
            }
            String payload = readPayloadDAO.readPayload(tradeId);
            String[] payloadData = payload.split(",");
            Trade trade = new Trade(payloadData[2], payloadData[3], payloadData[4], Integer.parseInt(payloadData[5]), tradeId);

            if (readPayloadDAO.isValidCUSIPSymbol(trade.cusip())) {
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
                String msg = " [*] Waiting for messages in '" + queueName + "'.";
                logger.info(msg);

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    String out = " [x] Received '"+ message + "'";
                    logger.info(out);
                    String tradeId = message.trim();
                    executeTrade(tradeId);
                };
                CancelCallback cancelCallback = consumerTag -> {
                };
                channel.basicConsume(queueName, true, deliverCallback, cancelCallback);

                synchronized (this){
                    while (!Thread.currentThread().isInterrupted()) {
                        try{
                            wait(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            logger.info("Consumer interrupted, shutting down...");
                        }
                    }
                }
            }
            return null;
        }
    }
}