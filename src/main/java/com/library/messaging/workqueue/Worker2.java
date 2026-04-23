package com.library.messaging.workqueue;

import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.*;

public class Worker2 {

    private static final String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        channel.basicQos(2);

        System.out.println("Worker2 waiting...");

        channel.basicConsume(QUEUE_NAME, false, (tag, delivery) -> {

            String message = new String(delivery.getBody());
            System.out.println("Worker2 processing: " + message);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

        }, tag -> {});
    }
}