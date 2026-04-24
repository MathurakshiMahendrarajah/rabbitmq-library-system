package com.library.producer;

import com.google.gson.Gson;
import com.library.config.MessagingTopology;
import com.library.config.RabbitMQConnection;
import com.library.model.Book;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class BookProducer {

    public static void main(String[] args) throws Exception {

        //User input for book details and action
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter action (ADD / BORROW / RETURN): ");
        String action = scanner.nextLine().trim().toUpperCase();

        System.out.println("Enter book title: ");
        String title = scanner.nextLine();

        System.out.println("Enter author: ");
        String author = scanner.nextLine();

        String routingKey = switch (action) {
            case "ADD" -> MessagingTopology.CMD_ADD;
            case "BORROW" -> MessagingTopology.CMD_BORROW;
            case "RETURN" -> MessagingTopology.CMD_RETURN;
            default -> null;
        };

        if (routingKey == null) {
            System.out.println("Invalid action. Use ADD / BORROW / RETURN");
            scanner.close();
            return;
        }

        // Create Book object with user input
        Book book = new Book(title, author, action);

        //Establish connection to RabbitMQ and create a channel
        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        MessagingTopology.declareTopology(channel);

        Gson gson = new Gson();
        String message = gson.toJson(book);

        channel.basicPublish(
                MessagingTopology.COMMAND_EXCHANGE,
                routingKey,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes(StandardCharsets.UTF_8)
        );

        System.out.println("Sent command [" + routingKey + "]: " + message);

        // Close channel and connection after sending message
        channel.close();
        connection.close();
        scanner.close();
    }
}