package com.library.messaging.direct;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.*;

public class BorrowConsumer {

    private static final String EXCHANGE_NAME = "direct_exchange";
    private static final String QUEUE_NAME = "borrow_queue";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        // declare exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        // declare queue
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // bind queue with routing key
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "book.borrow");

        System.out.println("Waiting for borrow messages...");

        // receive messages
        channel.basicConsume(QUEUE_NAME, true, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println("Received (Borrow): " + message);
        }, consumerTag -> {});
    }
}