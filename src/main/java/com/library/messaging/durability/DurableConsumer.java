package com.library.messaging.durability;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.*;

public class DurableConsumer {

    private static final String QUEUE_NAME = "durable_queue";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        // same durable queue
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        System.out.println("Waiting for durable messages...");

        channel.basicConsume(QUEUE_NAME, true, (tag, delivery) -> {
            String msg = new String(delivery.getBody());
            System.out.println("Received: " + msg);
        }, tag -> {});
    }
}