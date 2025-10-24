# MELI Order System

## 📘 Project Overview
The **MELI Order System** is a Spring Boot 3 application designed to simulate the management of online store orders.  

The system allows creating, viewing, updating, and deleting customer orders, all managed through a RESTful API.

---

## ⚙️ Technologies Used
- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **H2 In-Memory Database**
- **Lombok**
- **Maven**
- **Postman** (for API testing)

---

## 📁 Project Structure
```
CH5-Technoready/
 ├── src/
 │   ├── main/
 │   │   ├── java/com/techready/meli/
 │   │   │   ├── controller/OrderController.java
 │   │   │   ├── model/Order.java
 │   │   │   └── repository/OrderRepository.java
 │   │   └── resources/
 │   │       ├── application.yml
 │   │       ├── application-dev.yml
 │   │       ├── application-test.yml
 │   │       └── application-prod.yml
 ├── pom.xml
 ├── run-dev.bat
 └── run-dev.sh
```

---

## How to Run the Project

### 🔹 Prerequisites
- Java 17 or higher installed
- Maven installed and added to PATH

### 🔹 Running the Application

#### Windows
```bash
run-dev.bat
```

#### Linux / macOS
Make the script executable once:
```bash
chmod +x run-dev.sh
```

Then run:
```bash
./run-dev.sh
```

This will start the application in the **dev profile**, using an in-memory H2 database.

---

## API Endpoints

| Method | Endpoint | Description |
|--------|-----------|--------------|
| **POST** | `/api/orders` | Create a new order |
| **GET** | `/api/orders` | Retrieve all orders |
| **GET** | `/api/orders/{id}` | Retrieve a specific order |
| **PUT** | `/api/orders/{id}` | Update an existing order |
| **DELETE** | `/api/orders/{id}` | Delete an order |

### Example JSON Body
```json
{
  "customerName": "Mia Sánchez",
  "product": "Wireless Keyboard",
  "quantity": 1,
  "price": 599.99,
  "orderDate": "2025-10-17T10:00:00"
}
```

---

## Postman Collection
A Postman collection was developed to test all CRUD operations for the `/api/orders` resource, including:  
- Create (POST)  
- Read (GET)  
- Update (PUT)  
- Delete (DELETE)  

Each request includes example data and descriptions.

---

## Database Configuration
The project uses **H2 (in-memory)** as its database for quick setup.  
The configuration can be found in `application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:meli_db
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
server:
  port: 8080
```

H2 Console available at:  
👉 [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
JDBC URL: `jdbc:h2:mem:meli_db`
