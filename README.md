# 📚 RabbitMQ Library Management System

This is a simple console-based Library Management System built using Java and RabbitMQ.  
It demonstrates asynchronous communication using a message broker.

---

## 🚀 Features

- Add books to the library
- Borrow books
- Return books
- Track book availability using in-memory storage

---

## 🧠 Concepts Used

- RabbitMQ (Message Broker)
- Producer-Consumer Architecture
- Queue-based communication
- JSON messaging (using Gson)
- In-memory data structure (HashMap)

---

## 🔥 Advanced RabbitMQ Concepts (New)

As an extension, the project was enhanced to demonstrate multiple RabbitMQ messaging patterns:

### 1. Direct Exchange
- Routes messages based on exact routing keys  
- Example: `book.borrow`, `book.return`

### 2. Fanout Exchange
- Broadcasts messages to all connected consumers  
- Useful for notifications and logging

### 3. Topic Exchange
- Uses pattern-based routing with wildcards (`*`, `#`)  
- Example: `book.*`

### 4. Work Queue (Task Distribution)
- Multiple consumers share tasks  
- Demonstrates load balancing

### 5. Message Acknowledgment
- Ensures messages are processed reliably  
- Prevents message loss if a consumer fails

### 6. Durable Queues & Persistent Messages
- Ensures data is not lost even if RabbitMQ restarts  

---

## ⚙️ Technologies

- Java (JDK 17)
- RabbitMQ
- Maven
- Gson

---

## 🔄 Project Structure

```
RabbitMQ_Project/
├── src/main/java/com/library/
│   ├── config/                              # RabbitMQ connection & topology
│   │   ├── RabbitMQConnection.java
│   │   └── MessagingTopology.java           # Centralized topology constants
│   ├── model/                               # Data models
│   │   └── Book.java
│   ├── producer/                            # Main library producer
│   │   └── BookProducer.java
│   ├── consumer/                            # Main library command processor
│   │   └── BookConsumer.java
│   ├── messaging/                           # Production library system
│   │   └── event/                           # Event consumers for library
│   │       ├── AuditConsumer.java           # Audit all events
│   │       ├── NotificationConsumer.java    # Send notifications
│   │       └── AnalyticsConsumer.java       # Track statistics
│   └── examples/                            # Learning examples of patterns
│       ├── ack/                             # Message ACK demo
│       ├── direct/                          # Direct exchange demo
│       ├── durability/                      # Durable queues demo
│       ├── fanout/                          # Fanout exchange demo
│       ├── topic/                           # Topic exchange demo
│       ├── workqueue/                       # Work queue demo
│       └── README.md                        # Examples guide
├── pom.xml
└── README.md
```

---

## 📚 Two-Part Architecture

### 1. **Main Library Management System** (Production-like)

Located in `src/main/java/com/library/{producer,consumer,messaging}`

A fully functional event-driven library management system using:
- **Direct Exchange** for command routing (`book.add`, `book.borrow`, `book.return`)
- **Topic Exchange** for event broadcasting (`book.added`, `book.borrowed`, etc.)
- Multiple independent event consumers (Audit, Notification, Analytics)
- Durable queues, persistent messages, manual ACKs
- Proper command/event separation

### 2. **Learning Examples** (Demo Patterns)

Located in `src/main/java/com/library/examples/`

Standalone examples demonstrating each RabbitMQ pattern:
- Direct exchange routing
- Fanout broadcast
- Topic pattern matching
- Work queue load balancing
- Message durability
- Message acknowledgment

See `examples/README.md` for running each demo.

---

## 🐇 RabbitMQ Setup (Docker)

Run RabbitMQ with management UI:
```bash
  docker run -d --hostname rabbit --name rabbitmq \
  -p 5673:5672 -p 15673:15672 rabbitmq:3-management
```

Open UI:
```bash
  http://localhost:15673
```
Login:
```bash
username: guest
password: guest
```
---

## How to Run

Start RabbitMQ using Docker (if not already running):
```bash
docker run -d --hostname rabbit --name rabbitmq \
  -p 5673:5672 -p 15673:15672 rabbitmq:3-management
```

### Option A: Run Main Library Management System

**Terminal 1 - Compile:**
```bash
cd /home/mathurakshi/Documents/RabbitMQ_Project
mvn -q -DskipTests compile
```

**Terminal 2 - Command Processor (processes all add/borrow/return commands):**
```bash
java -cp target/classes com.library.consumer.BookConsumer
```

**Terminal 3 - Audit Consumer (logs all events):**
```bash
java -cp target/classes com.library.messaging.event.AuditConsumer
```

**Terminal 4 - Notification Consumer (alerts on borrow/return):**
```bash
java -cp target/classes com.library.messaging.event.NotificationConsumer
```

**Terminal 5 - Analytics Consumer (tracks statistics):**
```bash
java -cp target/classes com.library.messaging.event.AnalyticsConsumer
```

**Terminal 6+ - Producer (send commands):**
```bash
java -cp target/classes com.library.producer.BookProducer
```

**Demo Sequence (in Terminal 6+):**
```
Action: ADD
Title: Harry Potter
Author: J.K. Rowling

Action: ADD
Title: The Hobbit
Author: J.R.R. Tolkien

Action: BORROW
Title: Harry Potter
Author: (any)

Action: RETURN
Title: Harry Potter
Author: (any)

Action: BORROW
Title: NonExistent
Author: (any - will be rejected)
```

### Option B: Run Pattern Examples

See `src/main/java/com/library/examples/README.md` for running individual pattern demonstrations.

---

## Example

When running the library system, you'll see output like:

**BookConsumer (Command Processor) output:**
```
Waiting for library commands...
Book Added: Harry Potter
Borrowed: Harry Potter
Already borrowed: Harry Potter
Returned: Harry Potter
```

**AuditConsumer output:**
```
AUDIT => {"event":"book.added","title":"Harry Potter",...}
AUDIT => {"event":"book.borrowed","title":"Harry Potter",...}
```

**NotificationConsumer output:**
```
[14:32:05] 📧 NOTIFICATION: Harry Potter has been borrowed
[14:32:10] 📧 NOTIFICATION: Harry Potter has been returned and is now available
```

**AnalyticsConsumer output:**
```
📊 ANALYTICS UPDATE:
  Books Added: 2
  Books Borrowed: 1
  Books Returned: 1
  Rejections: 0
  Total Events: 4
```
