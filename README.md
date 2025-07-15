# Programming Laboratory Project – Server

## Overview
This repository contains the server-side application for the Programming Laboratory Project. The server is built with:

- **Java 11+**
- **Spring Boot** (for handling HTTP requests and application configuration)
- **Spring MVC** (for RESTful controllers)
- **Hibernate** (for ORM and optimized database interactions)

It exposes a REST API that the client application consumes. For the client code, see: [Programming Laboratory Project – Client](https://github.com/Antqnio/Programming-Laboratory-Project-Client).

## Features
- User authentication and authorization
- CRUD operations on project resources
- Data persistence using MySQL (or any JDBC-compatible) database
- Exception handling and logging

---

## Prerequisites
- **Java Development Kit (JDK) 11** or higher
- **Maven** 3.6+ (for building the project)
- **MySQL Server** (or another relational database supporting JDBC)
- **NetBeans IDE 8.2+** (recommended for easy project setup and execution)

---

## Configuration
Before running the application, you must configure your database connection settings in `application.properties`.

1. Open `src/main/resources/application.properties`.  
2. Locate the following properties:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
   spring.datasource.username=root
   spring.datasource.password=root
3. Replace root (for both username and password) with your own SQL credentials.
The database is automatically created by Spring(`spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/drivers?createDatabaseIfNotExist=true` in `src/main/resources/application.properties`).

---

## Building and Running
#### Using NetBeans IDE

1. Open NetBeans and choose File > Open Project, then select this repository’s root folder.
2. Right-click the project in the Projects pane and select Clean and Build.
3. After build completion, right-click again and choose Run. NetBeans will handle Maven goals automatically and start the server on http://localhost:8080/
