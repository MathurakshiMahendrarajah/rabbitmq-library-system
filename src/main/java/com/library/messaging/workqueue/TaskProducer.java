package com.library.messaging.workqueue;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class TaskProducer {

    private static final String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        // durable queue
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // send multiple tasks
        for (int i = 1; i <= 5; i++) {
            String message = "Task " + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("Sent: " + message);
        }

        channel.close();
        connection.close();
    }
}