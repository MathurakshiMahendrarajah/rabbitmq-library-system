package com.library.config;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;

public final class MessagingTopology {

    private MessagingTopology() {
    }

    public static final String COMMAND_EXCHANGE = "library.command.x";
    public static final String EVENT_EXCHANGE = "library.event.x";

    public static final String ADD_QUEUE = "library.add.q";
    public static final String BORROW_QUEUE = "library.borrow.q";
    public static final String RETURN_QUEUE = "library.return.q";
    public static final String AUDIT_QUEUE = "library.audit.q";
    public static final String NOTIFICATION_QUEUE = "library.notification.q";
    public static final String ANALYTICS_QUEUE = "library.analytics.q";

    public static final String CMD_ADD = "book.add";
    public static final String CMD_BORROW = "book.borrow";
    public static final String CMD_RETURN = "book.return";

    public static final String EVT_ADDED = "book.added";
    public static final String EVT_BORROWED = "book.borrowed";
    public static final String EVT_RETURNED = "book.returned";
    public static final String EVT_REJECTED = "book.rejected";

    public static void declareTopology(Channel channel) throws IOException {
        channel.exchangeDeclare(COMMAND_EXCHANGE, BuiltinExchangeType.DIRECT, true);
        channel.exchangeDeclare(EVENT_EXCHANGE, BuiltinExchangeType.TOPIC, true);

        channel.queueDeclare(ADD_QUEUE, true, false, false, null);
        channel.queueDeclare(BORROW_QUEUE, true, false, false, null);
        channel.queueDeclare(RETURN_QUEUE, true, false, false, null);
        channel.queueDeclare(AUDIT_QUEUE, true, false, false, null);
        channel.queueDeclare(NOTIFICATION_QUEUE, true, false, false, null);
        channel.queueDeclare(ANALYTICS_QUEUE, true, false, false, null);

        channel.queueBind(ADD_QUEUE, COMMAND_EXCHANGE, CMD_ADD);
        channel.queueBind(BORROW_QUEUE, COMMAND_EXCHANGE, CMD_BORROW);
        channel.queueBind(RETURN_QUEUE, COMMAND_EXCHANGE, CMD_RETURN);

        channel.queueBind(AUDIT_QUEUE, EVENT_EXCHANGE, "book.*");
        channel.queueBind(NOTIFICATION_QUEUE, EVENT_EXCHANGE, EVT_BORROWED);
        channel.queueBind(NOTIFICATION_QUEUE, EVENT_EXCHANGE, EVT_RETURNED);
        channel.queueBind(ANALYTICS_QUEUE, EVENT_EXCHANGE, "book.#");
    }
}
