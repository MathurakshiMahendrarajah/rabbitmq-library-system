# RabbitMQ Examples Guide

This folder contains standalone examples of different RabbitMQ messaging patterns. These are for learning and reference purposes.

## Structure

```
examples/
├── ack/                 - Message acknowledgment pattern (demo)
├── direct/              - Direct exchange pattern (demo)
├── durability/          - Durable queues and persistent messages (demo)
├── fanout/              - Fanout exchange pattern (demo)
├── topic/               - Topic exchange pattern (demo)
└── workqueue/           - Work queue / task distribution pattern (demo)
```

## Running Examples

Each example is self-contained and can be run independently. They use the shared `RabbitMQConnection` config.

### ACK Example
Learn how message acknowledgment works and message redelivery on failure.

```bash
# Terminal 1
java -cp target/classes com.library.examples.ack.AckConsumer

# Terminal 2
java -cp target/classes com.library.examples.ack.AckProducer
```

### Direct Exchange Example
Route messages to specific queues based on routing keys.

```bash
# Terminal 1
java -cp target/classes com.library.examples.direct.BorrowConsumer

# Terminal 2
java -cp target/classes com.library.examples.direct.ReturnConsumer

# Terminal 3
java -cp target/classes com.library.examples.direct.DirectProducer
```

### Fanout Exchange Example
Broadcast messages to all connected queues.

```bash
# Terminal 1
java -cp target/classes com.library.examples.fanout.EmailConsumer

# Terminal 2
java -cp target/classes com.library.examples.fanout.LogConsumer

# Terminal 3
java -cp target/classes com.library.examples.fanout.FanoutProducer
```

### Topic Exchange Example
Route messages using pattern-based routing keys (* and # wildcards).

```bash
# Terminal 1
java -cp target/classes com.library.examples.topic.BorrowOnlyConsumer

# Terminal 2
java -cp target/classes com.library.examples.topic.AllBookConsumer

# Terminal 3
java -cp target/classes com.library.examples.topic.TopicProducer
```

### Durability Example
Learn about durable queues and persistent messages.

```bash
# Terminal 1
java -cp target/classes com.library.examples.durability.DurableConsumer

# Terminal 2
java -cp target/classes com.library.examples.durability.DurableProducer
```

### Work Queue Example
Distribute tasks among multiple workers.

```bash
# Terminal 1
java -cp target/classes com.library.examples.workqueue.Worker1

# Terminal 2
java -cp target/classes com.library.examples.workqueue.Worker2

# Terminal 3
java -cp target/classes com.library.examples.workqueue.TaskProducer
```

## Key Concepts Demonstrated

- **Direct Exchange**: One-to-one message routing
- **Fanout Exchange**: Broadcasting to all subscribers
- **Topic Exchange**: Multi-criteria routing with wildcards
- **Work Queue**: Load balancing with multiple consumers
- **Durability**: Message and queue persistence
- **ACK**: Manual message acknowledgment and failure handling

