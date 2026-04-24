package com.library.examples.workqueue;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.*;

public class Worker1 {

    private static final String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // process one message at a time
        channel.basicQos(1);

        System.out.println("Worker1 waiting...");

        channel.basicConsume(QUEUE_NAME, false, (tag, delivery) -> {

            String message = new String(delivery.getBody());
            System.out.println("Worker1 processing: " + message);

            // simulate work
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // manual acknowledgment
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

        }, tag -> {});
    }
}

