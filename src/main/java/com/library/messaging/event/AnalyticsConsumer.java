package com.library.messaging.event;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.library.config.MessagingTopology;
import com.library.config.RabbitMQConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsConsumer {

    private static final Gson GSON = new Gson();
    private static final Map<String, Integer> eventCounts = new HashMap<>();

    static {
        eventCounts.put("book.added", 0);
        eventCounts.put("book.borrowed", 0);
        eventCounts.put("book.returned", 0);
        eventCounts.put("book.rejected", 0);
    }

    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        MessagingTopology.declareTopology(channel);
        channel.basicQos(1);

        System.out.println("Analytics consumer waiting for all library events...");

        channel.basicConsume(MessagingTopology.ANALYTICS_QUEUE, false, (tag, delivery) -> {
            String eventJson = new String(delivery.getBody(), StandardCharsets.UTF_8);
            try {
                JsonObject event = GSON.fromJson(eventJson, JsonObject.class);
                String eventType = event.get("event").getAsString();

                eventCounts.put(eventType, eventCounts.getOrDefault(eventType, 0) + 1);

                System.out.println("\n📊 ANALYTICS UPDATE:");
                System.out.println("  Books Added: " + eventCounts.get("book.added"));
                System.out.println("  Books Borrowed: " + eventCounts.get("book.borrowed"));
                System.out.println("  Books Returned: " + eventCounts.get("book.returned"));
                System.out.println("  Rejections: " + eventCounts.get("book.rejected"));
                System.out.println("  Total Events: " + eventCounts.values().stream().mapToInt(Integer::intValue).sum());
                System.out.println();

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                System.err.println("Error processing analytics: " + e.getMessage());
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
            }
        }, tag -> {});
    }
}

