package org.pavani.multithreading.trading_multithreading.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.pavani.multithreading.trading_multithreading.config.RabbitMQConfig;
import org.pavani.multithreading.trading_multithreading.util.ApplicationConfigProperties;

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
    private static final String EXCHANGE_NAME = "trade_MQ";

    private final List<LinkedBlockingQueue<String>> queues;
    Map<String, LinkedBlockingQueue<String>> resultQueues;
    ConnectionFactory factory;
    int roundRobinIndex = 0;
    RabbitMQConfig rabbitMQConfig;
    private static ApplicationConfigProperties applicationConfigProperties;

    public TradeDistributionQueueService(int numberOfQueues) {
        applicationConfigProperties = ApplicationConfigProperties.getInstance();
        queues = new ArrayList<>(numberOfQueues);
        resultQueues = new HashMap<>();
        for (int i = 0; i < numberOfQueues; i++) {
            LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
            queues.add(queue);
            resultQueues.put("q" + (i + 1), queue);
        }
        rabbitMQConfig = new RabbitMQConfig();
        factory = rabbitMQConfig.connect();
    }

    public Map<String, LinkedBlockingQueue<String>> getResultQueues() {
        return resultQueues;
    }

    public int getQueueIndex(ConcurrentMap<String, String> resultMap, String accNumber) {
        int queueIndex;
        if(resultMap.containsKey(accNumber)) {
            queueIndex = Integer.parseInt(resultMap.get(accNumber).substring(1))-1;
        }
        else{
        queueIndex = roundRobinIndex;
        roundRobinIndex = (roundRobinIndex + 1) % queues.size();
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
                        processQueue(queueIndex, tradeId);
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

    private void processQueue(int queueIndex, String tradeId) {
        if(applicationConfigProperties.getUseRabbitMQ()){
            // Producer class
            // Establish connection and create channel
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.exchangeDeclare(EXCHANGE_NAME, "direct");
                // Declare an exchange of type direct and publish
                String routingKey = "trading_queue_"+ queueIndex;
                channel.queueDeclare(routingKey, true, false, false, null);
                channel.queueBind(routingKey, EXCHANGE_NAME, routingKey);
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, tradeId.getBytes("UTF-8"));
                System.out.println(" Trade_id " + tradeId +" sent to queue trade_queue_"+ queueIndex);
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        } else{
            queues.get(queueIndex).add(tradeId);
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