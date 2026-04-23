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

## 🔄 How It Works

1. The **Producer** takes user input (ADD / BORROW / RETURN).
2. It converts the data into JSON format.
3. The message is sent to a RabbitMQ queue (`library_queue`).
4. The **Consumer** listens to the queue.
5. When a message is received:
   - It is converted back into a Java object.
   - The action is processed.
   - Book availability is updated in memory.

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

1. Start RabbitMQ
2. Run BookConsumer.java
3. Run BookProducer.java
4. Enter actions in console

---

## Example

Input:
```bash
  ADD
  Harry Potter
  J.K. Rowling
```
Output:
```bash
  Book Added: Harry Potter
```

