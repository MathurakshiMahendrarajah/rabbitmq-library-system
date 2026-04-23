package com.library.messaging.topic;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.*;

public class AllBookConsumer {

    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String queueName = channel.queueDeclare().getQueue();

        // listen to ALL book related events
        channel.queueBind(queueName, EXCHANGE_NAME, "book.*");

        System.out.println("Waiting for all book messages...");

        channel.basicConsume(queueName, true, (tag, delivery) -> {
            String msg = new String(delivery.getBody());
            System.out.println("AllBookConsumer: " + msg);
        }, tag -> {});
    }
}