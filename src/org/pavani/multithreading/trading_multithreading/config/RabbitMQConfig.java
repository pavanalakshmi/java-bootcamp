package org.pavani.multithreading.trading_multithreading.config;

import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConfig {
    public ConnectionFactory connect(){
        // Setup connection factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // Or the RabbitMQ server IP/hostname
        factory.setUsername("guest"); // RabbitMQ username
        factory.setPassword("guest"); // RabbitMQ password

        return factory;
    }
}
