# Trylma Game

A Java implementation of the classic Chinese Checkers (Trylma) game, featuring a client-server architecture using Spring Boot (server) and JavaFX (client). The server communicates with a MySQL database.

## Authors

- Błażej Pawluk
- Tomasz Stefaniak

## Features

- Multiplayer Chinese Checkers gameplay
- JavaFX graphical client
- Spring Boot server with REST API
- MySQL database integration for persistence

## Requirements

- Java 21
- Maven 3.8+
- MySQL 8.x

## Project Structure

- `/trylma` &mdash; Parent Maven project
  - `/client` &mdash; JavaFX-based game client
  - `/server` &mdash; Spring Boot server

## Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/tomekstefaniak/trylma-game.git
cd trylma-game
```

### 2. Set up the MySQL Database

Create a database named `trylma` and configure a user (default: `root`, password: `haslo`). You can change these settings in `trylma/server/src/main/resources/application.properties`.

Example database setup:

```sql
CREATE DATABASE trylma CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'haslo';
GRANT ALL PRIVILEGES ON trylma.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

Ensure your `application.properties` contains:

```
spring.datasource.url=jdbc:mysql://localhost:3306/trylma
spring.datasource.username=root
spring.datasource.password=haslo
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Build the Project

Build all modules from the root directory:

```bash
mvn clean install
```

Or build only the server/client:

```bash
cd trylma/server
mvn clean package

cd ../client
mvn clean package
```

### 4. Run the Server

From the `trylma/server` directory, use Maven to launch the Spring Boot server:

```bash
mvn spring-boot:run
```

Or run the generated JAR:

```bash
java -jar target/server-1.0-SNAPSHOT.jar
```

### 5. Run the Client

From the `trylma/client` directory, start the JavaFX client:

```bash
mvn javafx:run
```

Or run the JAR directly (if JavaFX is available):

```bash
java -jar target/client-1.0-SNAPSHOT.jar
```

### 6. Configuration

- Server main class: `server.trylma.DbApp`
- Client main class: `client.trylma.ClientApp`
- All configuration files are in `trylma/server/src/main/resources/application.properties`

## Notes

- Make sure your JavaFX version matches your Java version.
- Customize database credentials as needed.
- For development, you may use included scripts (see `/scripts`).

## License

MIT License
