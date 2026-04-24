package com.library.messaging.event;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.library.config.MessagingTopology;
import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationConsumer {

    private static final Gson GSON = new Gson();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        MessagingTopology.declareTopology(channel);
        channel.basicQos(1);

        System.out.println("Notification consumer waiting for borrow/return events...");

        channel.basicConsume(MessagingTopology.NOTIFICATION_QUEUE, false, (tag, delivery) -> {
            String eventJson = new String(delivery.getBody(), StandardCharsets.UTF_8);
            try {
                JsonObject event = GSON.fromJson(eventJson, JsonObject.class);
                String eventType = event.get("event").getAsString();
                String title = event.get("title").getAsString();
                String time = LocalDateTime.now().format(formatter);

                if ("book.borrowed".equals(eventType)) {
                    System.out.println("[" + time + "] 📧 NOTIFICATION: " + title + " has been borrowed");
                } else if ("book.returned".equals(eventType)) {
                    System.out.println("[" + time + "] 📧 NOTIFICATION: " + title + " has been returned and is now available");
                }

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                System.err.println("Error processing notification: " + e.getMessage());
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
            }
        }, tag -> {});
    }
}


