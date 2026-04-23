package com.library.messaging.topic;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.*;

public class BorrowOnlyConsumer {

    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String queueName = channel.queueDeclare().getQueue();

        // listen ONLY to borrow events
        channel.queueBind(queueName, EXCHANGE_NAME, "book.borrow");

        System.out.println("Waiting for borrow messages...");

        channel.basicConsume(queueName, true, (tag, delivery) -> {
            String msg = new String(delivery.getBody());
            System.out.println("BorrowOnlyConsumer: " + msg);
        }, tag -> {});
    }
}