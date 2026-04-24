package com.library.examples.direct;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.*;

public class ReturnConsumer {

    private static final String EXCHANGE_NAME = "direct_exchange";
    private static final String QUEUE_NAME = "return_queue";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        // declare exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        // declare queue
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // bind queue with routing key
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "book.return");

        System.out.println("Waiting for return messages...");

        // receive messages
        channel.basicConsume(QUEUE_NAME, true, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println("Received (Return): " + message);
        }, consumerTag -> {});
    }
}

