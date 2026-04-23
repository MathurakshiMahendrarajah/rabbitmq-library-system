package com.library.messaging.durability;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.*;

public class DurableProducer {

    private static final String QUEUE_NAME = "durable_queue";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        // durable queue (IMPORTANT)
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        String message = "Persistent Message";

        // mark message as persistent
        channel.basicPublish(
                "",
                QUEUE_NAME,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes()
        );

        System.out.println("Sent: " + message);

        channel.close();
        connection.close();
    }
}