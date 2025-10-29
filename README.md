# MELI Order System

## 📘 Project Overview
The **MELI Order System** is a Spring Boot 3 application designed to simulate the management of online store orders.  

The system allows creating, viewing, updating, and deleting customer orders, all managed through a RESTful API.

**Main Features:**
- Create, read, update, and delete customer orders.
- Integrated H2 database for local development.
- PostgreSQL support for production environments.
- Environment-specific profiles (`dev`, `test`, `prod`) using YAML configuration.
- Secure environment variable management via `.env` files.


## ⚙️ Technologies Used
- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **H2 In-Memory Database**
- **Lombok**
- **Maven**
- **Postman** (for API testing)


## 📁 Project Structure
```
CH5-Technoready/
├── src/
│ └── main/
│     ├── java/com/techready/meli/
│     │     ├── controller/OrderController.java
│     │     ├── model/Order.java
│     │     └── repository/OrderRepository.java
│     │
│     └── resources/
│           ├── application.yml
│           ├── application-dev.yml
│           ├── application-test.yml
│           └── application-prod.yml
│ 
├── .env
├── .env.example
├── startup.sh
└── pom.xml
```

## How to Run the Project

### Prerequisites
- Java 17 or higher installed
- Maven installed and added to PATH
### Running the Application
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


## API Endpoints

| Method | Endpoint | Description |
|--------|-----------|--------------|
| **POST** | `/api/orders` | Create a new order |
| **GET** | `/api/orders` | Retrieve all orders |
| **GET** | `/api/orders/{id}` | Retrieve a single order by ID|
| **PUT** | `/api/orders/{id}` | Update an existing order |
| **DELETE** | `/api/orders/{id}` | Delete an order by ID |

### Example JSON Body
```json
{
  "customerName": "Ana Lilia Tiznado",
  "product": "Wireless Mouse",
  "quantity": 1,
  "price": 79.99,
  "orderDate": "2025-10-17T10:00:00"
}
```

## Postman Collection
A Postman collection was developed to test all CRUD operations for the `/api/orders` resource, including:  
- Create (POST)  
- Read (GET)  
- Update (PUT)  
- Delete (DELETE)  

Each request includes example data and descriptions.

---

## Database Configuration
### 🧪 Development Profile
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

---
### 🧪 Test profile
Uses H2 in-memory database with create-drop to auto-reset between test runs.
Configured for debugging and validation.

---


### 🚀 Production profile
Uses PostgreSQL for persistent storage.
Reads credentials from system variables or `.env` 

```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/meli_prod
    username: ${POSTGRES_USER:postgres}
    password: ${POSTGRES_PASSWORD:admin}
```

#### 🔐 Environment variables
Environment variables are managed via a `.env` file in the project root.
Example `.env`
```bash
# --- DEV (H2)
DB_URL=jdbc:h2:mem:meli_db
DB_USERNAME=sa
DB_PASSWORD=

# --- PROD (PostgreSQL)
POSTGRES_USER=postgres
POSTGRES_PASSWORD=admin
```

Example `.env.example`
```bash
# Copy this file to .env and fill with your credentials

SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
LOG_LEVEL=INFO

# --- DEV (H2)
DB_URL=jdbc:h2:mem:meli_db
DB_USERNAME=sa
DB_PASSWORD=

# --- PROD (PostgreSQL)
# DB_URL=jdbc:postgresql://localhost:5432/meli_prod
# POSTGRES_USER=postgres
# POSTGRES_PASSWORD=admin
```

## 🔄 Switching Profiles

You can switch profiles in one of two ways:

### Option 1 – Using Maven
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
mvn spring-boot:run -Dspring-boot.run.profiles=test
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Option 2 – Inside `.env`

Set the active profile directly:
```bash
SPRING_PROFILES_ACTIVE=prod
```

## 🚀 Startup Script
The included run-dev.sh file launches the project automatically:
```bash
#!/bin/bash
echo "Starting MELI Order System..."
source .env
mvn spring-boot:run -Dspring-boot.run.profiles=$SPRING_PROFILES_ACTIVE
```

Make it executable:
```bash
chmod +x run-dev.sh
```

Then run:
```
./run-dev.sh
```

