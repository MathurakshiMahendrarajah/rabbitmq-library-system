package com.library.messaging.fanout;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.*;

public class LogConsumer {

    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // temporary queue
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("Log Consumer waiting...");

        channel.basicConsume(queueName, true, (tag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println("Log Saved: " + message);
        }, tag -> {});
    }
}