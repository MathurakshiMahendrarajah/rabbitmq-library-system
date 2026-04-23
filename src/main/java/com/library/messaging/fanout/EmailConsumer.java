package com.library.messaging.fanout;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.*;

public class EmailConsumer {

    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // create temporary queue
        String queueName = channel.queueDeclare().getQueue();

        // bind queue to exchange
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("Email Consumer waiting...");

        channel.basicConsume(queueName, true, (tag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println("Email Sent: " + message);
        }, tag -> {});
    }
}