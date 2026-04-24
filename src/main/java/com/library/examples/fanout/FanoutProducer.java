package com.library.examples.fanout;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class FanoutProducer {

    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        // declare fanout exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String message = "New Book Added!";

        // routing key is ignored in fanout
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());

        System.out.println("Sent: " + message);

        channel.close();
        connection.close();
    }
}

