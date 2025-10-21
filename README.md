# Notification Service
>Notification Service handles all communication and alerts in Rezervero. It receives events from Booking Service via RabbitMQ and sends real-time notifications and emails to 
> clients and mechanics regarding booking status changes.

Key features:
- Event Handling: Receive booking events from Booking Service through RabbitMQ.
- Email Notifications: Send emails about booking confirmations, cancellations, or updates.
- Real-Time Updates: Deliver WebSocket notifications to clients and mechanics for immediate feedback.

## Table of Contents
* [Technologies Used](#technologies-used)
* [Setup](#setup)
* [Contact](#contact)

## Technologies Used
- Spring Boot 3
- RabbitMQ
- WebSocket
- Thymeleaf
- MailSender
- Docker

## Setup
0. **Before starting:** You must have docker installed and running

1. Clone the repository:
```bash
git clone https://github.com/jakubknap/Rezerveo-Notification.git
```

2. Go to the project directory and start the containers with the command:
```bash
docker compose up -d
```

3. Run the application
```bash
mvn spring-boot:run
```

4. **[Optional]** Run the Spring Boot application using the java -jar command after building through maven<br>
   Go to the project directory and run app with the command:
```bash
java -jar .\target\rezerveo-notification-1.0.0.jar
```

## Contact
Created by [Jakub Knap](https://www.linkedin.com/in/jakub-knap/) - feel free to contact me!