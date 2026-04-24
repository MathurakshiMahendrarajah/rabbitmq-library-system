package com.library.examples.topic;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class TopicProducer {

    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        // declare topic exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // messages with different routing keys
        channel.basicPublish(EXCHANGE_NAME, "book.borrow", null, "Borrow Book".getBytes());
        channel.basicPublish(EXCHANGE_NAME, "book.return", null, "Return Book".getBytes());
        channel.basicPublish(EXCHANGE_NAME, "user.create", null, "New User Created".getBytes());

        System.out.println("Messages Sent");

        channel.close();
        connection.close();
    }
}

