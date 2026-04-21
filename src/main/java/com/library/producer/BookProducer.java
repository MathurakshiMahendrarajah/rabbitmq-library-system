package com.library.producer;

import com.google.gson.Gson;
import com.library.config.RabbitMQConnection;
import com.library.model.Book;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.Scanner;

public class BookProducer {

    //Name of the RabbitMQ queue
    private final static String QUEUE_NAME = "library_queue";

    public static void main(String[] args) throws Exception {

        //User input for book details and action
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter action (ADD / BORROW / RETURN): ");
        String action = scanner.nextLine();

        System.out.println("Enter book title: ");
        String title = scanner.nextLine();

        System.out.println("Enter author: ");
        String author = scanner.nextLine();

        // Create Book object with user input
        Book book = new Book(title, author, action);

        //Establish connection to RabbitMQ and create a channel
        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        //Declare queue
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Gson gson = new Gson();
        String message = gson.toJson(book);

        // Publish message to queue using default exchange ("")
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println("Sent: " + message);

        // Close channel and connection after sending message
        channel.close();
        connection.close();
    }
}