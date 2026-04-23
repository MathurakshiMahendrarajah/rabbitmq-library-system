package com.library.messaging.ack;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.*;

public class AckConsumer {

    private static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println("Waiting for messages...");

        channel.basicConsume(QUEUE_NAME, false, (tag, delivery) -> {

            String message = new String(delivery.getBody());
            System.out.println("Processing: " + message);

            // simulate failure
            if (true) {
                System.out.println("Simulating crash... No ACK sent!");
                return;
            }

            // ACK (this will NOT execute)
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

        }, tag -> {});
    }
}