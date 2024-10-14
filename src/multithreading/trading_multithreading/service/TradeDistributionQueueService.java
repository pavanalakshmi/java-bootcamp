package multithreading.trading_multithreading.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

public class TradeDistributionQueueService implements TradeDistributionQueue {
    private static final String EXCHANGE_NAME = "trade_distribution_queue";

    private final List<LinkedBlockingQueue<String>> queues;
    Map<String, LinkedBlockingQueue<String>> resultQueues;
    ApplicationConfigProperties applicationConfigProperties;
    ConnectionFactory factory;

    public TradeDistributionQueueService(int numberOfQueues) {
        applicationConfigProperties = new ApplicationConfigProperties();
        queues = new ArrayList<>(numberOfQueues);
        resultQueues = new HashMap<>();
        for (int i = 0; i < numberOfQueues; i++) {
            LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
            queues.add(queue);
            resultQueues.put("q" + (i + 1), queue);
        }
        factory = new ConnectionFactory();
        factory.setHost("localhost"); // Or the RabbitMQ server IP/hostname
        factory.setUsername("guest"); // RabbitMQ username
        factory.setPassword("guest"); // RabbitMQ password
    }

    public Map<String, LinkedBlockingQueue<String>> getResultQueues() {
        return resultQueues;
    }

    public int getQueueIndex(ConcurrentMap<String, String> resultMap, String accNumber) {
        int queueIndex = 1;
        if(resultMap.containsKey(accNumber)) {
            queueIndex = Integer.parseInt(resultMap.get(accNumber).substring(1))-1;
        }
        return queueIndex;
    }

    public synchronized void distributeQueue(String file, ConcurrentMap<String, String> resultMap) {
        String line;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while ((line = fileReader.readLine()) != null) {
                String tradeId = line.split(",")[0];
                String accNumber = line.split(",")[2];
                int queueIndex = getQueueIndex(resultMap,accNumber);
                if (queueIndex >= 0 && queueIndex < queues.size()) {
                    try{
                        if(applicationConfigProperties.loadQueueCriteria().equals("rabbitMQ")){
                            // Establish connection and create channel
                            try (Connection connection = factory.newConnection();
                                 Channel channel = connection.createChannel()) {
                                // Declare an exchange of type direct and publish
                                String routingKey = "trading_queue_"+queueIndex;
                                channel.exchangeDeclare(EXCHANGE_NAME, "direct");
                                channel.queueDeclare(routingKey, true, false, false, null);
                                channel.queueBind(routingKey, EXCHANGE_NAME, routingKey);
                                channel.basicPublish(EXCHANGE_NAME, routingKey, null, tradeId.getBytes("UTF-8"));
                                System.out.println(" Trade_id " +tradeId+" sent to queue trade_queue_"+queueIndex);
                            } catch (IOException | TimeoutException e) {
                                throw new RuntimeException(e);
                            }
                        } else{
                            queues.get(queueIndex).add(tradeId);
                        }
                    } catch (Exception e) {
                        System.out.println("Interrupted while adding trade ID to queue: " + tradeId);
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.err.println("Invalid queue index: " + queueIndex + " for trade ID: " + tradeId);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void distributeQueueWithoutMap(String file) {
        String line;
        int queueIndex = 0;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while ((line = fileReader.readLine()) != null) {
                String tradeId = line.split(",")[0];
                // assign trades in a round-robin manner
                if (queueIndex >= queues.size()) {
                    queueIndex = 0; // Reset the queue index to cycle through queues
                }
                queues.get(queueIndex).add(tradeId);
                queueIndex++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file in distributeQueueWithoutMap: "+e);
        }
    }
}
/**
 * Producer --


 // Simulate getting routing key based on credit card number
 private static String getRoutingKeyBasedOnCreditCard(int transactionId) {
 // For simplicity, route based on the transaction ID, e.g., to mimic credit card
 // partitioning
 return "cc_partition_" + (transactionId % 3);
 }


 */