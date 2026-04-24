package com.library.examples.direct;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class DirectProducer {

    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception {

        // create connection
        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        // declare direct exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        // routing keys
        String routingKey1 = "book.borrow";
        String routingKey2 = "book.return";

        // messages
        String message1 = "Borrow Book Request";
        String message2 = "Return Book Request";

        // send messages
        channel.basicPublish(EXCHANGE_NAME, routingKey1, null, message1.getBytes());
        System.out.println("Sent: " + message1);

        channel.basicPublish(EXCHANGE_NAME, routingKey2, null, message2.getBytes());
        System.out.println("Sent: " + message2);

        channel.close();
        connection.close();
    }
}

