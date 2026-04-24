package com.library.consumer;

import com.google.gson.Gson;
import com.library.config.MessagingTopology;
import com.library.config.RabbitMQConnection;
import com.library.model.Book;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.MessageProperties;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BookConsumer {

    //In-memory storage to track book availability (true = available, false = borrowed)
    private static Map<String, Boolean> library = new HashMap<>();
    private static final Gson GSON = new Gson();

    public static void main(String[] args) throws Exception {

        //Establish connection to RabbitMQ and create a channel
        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        MessagingTopology.declareTopology(channel);
        channel.basicQos(1);

        System.out.println("Waiting for library commands...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> processCommand(channel, delivery);

        channel.basicConsume(MessagingTopology.ADD_QUEUE, false, deliverCallback, consumerTag -> {});
        channel.basicConsume(MessagingTopology.BORROW_QUEUE, false, deliverCallback, consumerTag -> {});
        channel.basicConsume(MessagingTopology.RETURN_QUEUE, false, deliverCallback, consumerTag -> {});
    }

    private static void processCommand(Channel channel, Delivery delivery) {
        long deliveryTag = delivery.getEnvelope().getDeliveryTag();

        try {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Book book = GSON.fromJson(message, Book.class);

            String eventKey;
            String result;

            if (book == null || book.getAction() == null || book.getTitle() == null) {
                eventKey = MessagingTopology.EVT_REJECTED;
                result = "Invalid message payload";
            } else {
                switch (book.getAction().toUpperCase()) {
                    case "ADD":
                        library.put(book.getTitle(), true);
                        eventKey = MessagingTopology.EVT_ADDED;
                        result = "Book Added: " + book.getTitle();
                        break;

                    case "BORROW":
                        if (!library.containsKey(book.getTitle())) {
                            eventKey = MessagingTopology.EVT_REJECTED;
                            result = "Book not found: " + book.getTitle();
                        } else if (!library.get(book.getTitle())) {
                            eventKey = MessagingTopology.EVT_REJECTED;
                            result = "Already borrowed: " + book.getTitle();
                        } else {
                            library.put(book.getTitle(), false);
                            eventKey = MessagingTopology.EVT_BORROWED;
                            result = "Borrowed: " + book.getTitle();
                        }
                        break;

                    case "RETURN":
                        if (!library.containsKey(book.getTitle())) {
                            eventKey = MessagingTopology.EVT_REJECTED;
                            result = "Book not found: " + book.getTitle();
                        } else {
                            library.put(book.getTitle(), true);
                            eventKey = MessagingTopology.EVT_RETURNED;
                            result = "Returned: " + book.getTitle();
                        }
                        break;

                    default:
                        eventKey = MessagingTopology.EVT_REJECTED;
                        result = "Unknown action";
                }
            }

            publishEvent(channel, book, eventKey, result);
            System.out.println(result);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (Exception ignored) {
            }
        }
    }

    private static void publishEvent(Channel channel, Book book, String eventKey, String result) throws Exception {
        Map<String, String> event = new LinkedHashMap<>();
        event.put("event", eventKey);
        event.put("title", book != null ? book.getTitle() : "");
        event.put("author", book != null ? book.getAuthor() : "");
        event.put("action", book != null ? book.getAction() : "");
        event.put("message", result);
        event.put("time", Instant.now().toString());

        String eventJson = GSON.toJson(event);
        channel.basicPublish(
                MessagingTopology.EVENT_EXCHANGE,
                eventKey,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                eventJson.getBytes(StandardCharsets.UTF_8)
        );
    }
}