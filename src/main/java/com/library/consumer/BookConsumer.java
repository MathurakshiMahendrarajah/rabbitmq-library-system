package com.library.consumer;

import com.google.gson.Gson;
import com.library.config.RabbitMQConnection;
import com.library.model.Book;
import com.rabbitmq.client.*;
import java.util.HashMap;
import java.util.Map;

public class BookConsumer {

    //Name of the RabbitMQ queue
    private final static String QUEUE_NAME = "library_queue";

    //In-memory storage to track book availability (true = available, false = borrowed)
    private static Map<String, Boolean> library = new HashMap<>();

    public static void main(String[] args) throws Exception {

        //Establish connection to RabbitMQ and create a channel
        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        //Declare queue
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println("Waiting for messages...");

        //Callback function that runs whenever a message is received
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            //Convert message body (byte array) to String
            String message = new String(delivery.getBody());

            //Convert JSON string back to Book object
            Gson gson = new Gson();
            Book book = gson.fromJson(message, Book.class);

            switch (book.getAction().toUpperCase()) {

                case "ADD":
                    library.put(book.getTitle(), true);
                    System.out.println("Book Added: " + book.getTitle());
                    break;

                case "BORROW":
                    if (!library.containsKey(book.getTitle())) {
                        System.out.println("Book not found: " + book.getTitle());
                    } else if (!library.get(book.getTitle())) {
                        System.out.println("Already borrowed: " + book.getTitle());
                    } else {
                        library.put(book.getTitle(), false);
                        System.out.println("Borrowed: " + book.getTitle());
                    }
                    break;

                case "RETURN":
                    if (!library.containsKey(book.getTitle())) {
                        System.out.println("Book not found: " + book.getTitle());
                    } else {
                        library.put(book.getTitle(), true);
                        System.out.println("Returned: " + book.getTitle());
                    }
                    break;

                default:
                    System.out.println("Unknown action");
            }
        };

        // Start consuming messages from the queue
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}