package com.library.examples.ack;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class AckProducer {

    private static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String message = "Important Task";

        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println("Sent: " + message);

        channel.close();
        connection.close();
    }
}

