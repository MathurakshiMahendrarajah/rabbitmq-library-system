package com.library.messaging.event;

import com.library.config.MessagingTopology;
import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;

public class AuditConsumer {

    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        MessagingTopology.declareTopology(channel);
        channel.basicQos(1);

        System.out.println("Audit consumer waiting for library events...");

        channel.basicConsume(MessagingTopology.AUDIT_QUEUE, false, (tag, delivery) -> {
            String event = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("AUDIT => " + event);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }, tag -> {});
    }
}
