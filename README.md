# MELI Order System

## 📘 Project Overview
The **MELI Order System** is a Spring Boot 3 application designed to simulate the management of online store orders.

The system allows creating, viewing, updating, deleting, and **searching** customer orders, all managed through a RESTful API.

**Main Features:**
- Create, read, update, delete, and search customer orders.
- Integrated H2 database for local development.
- PostgreSQL support for production environments.
- Environment-specific profiles (`dev`, `test`, `prod`) using YAML configuration.
- Secure environment variable management via `.env` files.
- Dynamic endpoint for **filtering and searching** orders by text, numeric, or date parameters.

---

## ⚙️ Technologies
- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **PostgreSQL / H2**
- **Lombok**
- **Maven**
- **Postman** (for testing)

---

## 📁 Project Structure
```
CH5-Technoready/
├── src/
│   └── main/
│       ├── java/com/techready/meli/
│       │   ├── controller/OrderController.java
│       │   ├── model/Order.java
│       │   └── repository/OrderRepository.java
│       │
│       └── resources/
│           ├── application.yml
│           ├── application-dev.yml
│           ├── application-test.yml
│           └── application-prod.yml
│
├── .env
├── .env.example
├── run-meli.bat
├── run-meli.sh
└── pom.xml
```

---

## 🚀 API Endpoints

| Method | Endpoint | Description |
|--------|-----------|--------------|
| **POST** | `/api/orders` | Create a new order |
| **GET** | `/api/orders` | Retrieve all orders |
| **GET** | `/api/orders/{id}` | Retrieve a single order by ID |
| **PUT** | `/api/orders/{id}` | Update an existing order |
| **DELETE** | `/api/orders/{id}` | Delete an order by ID |
| **GET** | `/api/orders/search` | Search and filter orders|

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

---

## 🔍 Search & Filter Endpoint

### Endpoint
```
GET /api/orders/search
```

### Optional Query Parameters
| Parameter | Type | Description |
|------------|------|--------------|
| `customerName` | String | Partial match search by customer name |
| `product` | String | Partial match by product name |
| `minQuantity` | Integer | Orders with quantity ≥ value |
| `maxPrice` | Double | Orders with price ≤ value |
| `startDate` | ISO Date Time | e.g. `2025-10-23T00:00:00` |
| `endDate` | ISO Date Time | e.g. `2025-10-30T23:59:59` |

### Examples
```bash
# Get all orders
GET /api/orders/search

# Filter by customer name
GET /api/orders/search?customerName=Scarlett

# Filter by price and date range
GET /api/orders/search?maxPrice=900&startDate=2025-10-23T00:00:00&endDate=2025-10-30T23:59:59
```

### Example Response
```json
[
  {
    "id": 1,
    "customerName": "Scarlett",
    "product": "Wireless Headphones",
    "quantity": 2,
    "price": 799.99,
    "orderDate": "2025-10-23T15:55:00"
  }
]
```

---

## 🧪 Postman Collection
The updated Postman collection (`MELI-OrderSystem.postman_collection.json`) includes:
- CRUD tests
- Search & filter tests
- Validations for responses (`200 OK`, `204 No Content`, `400 Bad Request`)
- Example query parameters

---

## 🗄️ Database Configuration

### 🧪 Development Profile (H2)
The project uses **H2 (in-memory)** for local testing and fast startup.  
Configuration (from `application-dev.yml`):

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

H2 Console → [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
JDBC URL → `jdbc:h2:mem:meli_db`

---

### 🧩 Test Profile
Used for integration testing.  
Can be configured with H2 or PostgreSQL depending on test environment.  
Automatically resets data between test runs.

---

### 🚀 Production Profile
Uses **PostgreSQL** for persistent storage.  
Reads credentials from system variables or `.env`.

```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/meli_prod
    username: ${POSTGRES_USER:postgres}
    password: ${POSTGRES_PASSWORD:admin}
```

---

### 🔐 Environment Variables
Managed via `.env` file at project root.

`.env`
```bash
# --- DEV (H2)
DB_URL=jdbc:h2:mem:meli_db
DB_USERNAME=sa
DB_PASSWORD=

# --- PROD (PostgreSQL)
POSTGRES_USER=postgres
POSTGRES_PASSWORD=admin
```

`.env.example`
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

---

## 🔄 Switching Profiles

You can switch profiles in one of two ways:

### Option 1 – Maven
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
mvn spring-boot:run -Dspring-boot.run.profiles=test
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Option 2 – Environment File
Set the active profile directly:
```bash
SPRING_PROFILES_ACTIVE=prod
```

---

### 🚀 Startup Scripts
Scripts automatically load the `.env` variables and start the appropriate profile.

Windows:
```bash
run-meli.bat
```

Linux / Mac:
```bash
./run-meli.sh
```


---

## 🧭 API Documentation (Swagger / OpenAPI)

The MELI Order System is fully documented using **Swagger (OpenAPI 3.0)**.  
Swagger provides an interactive interface to explore, test, and visualize the API endpoints.

### 🧩 Access Points

| Type | URL |
|------|-----|
| Swagger UI (interactive) | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) |
| Raw OpenAPI JSON | [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) |

> If you are using a different port (e.g. `8082`), adjust the URL accordingly.

### 🧱 Features

- Auto-generated documentation for all endpoints (`/api/orders`, `/api/orders/{id}`, `/api/orders/search`)
- Request & response schemas included automatically
- Supports live testing via the **"Try it out"** feature
- Integrated with profiles (`dev`, `test`, `prod`)

### 🧰 Custom Configuration
The configuration file is located at:
``src/main/java/com/techready/meli/config/OpenApiConfig.java``


This file defines:
- API title and description
- Versioning information
- Contact details
- License reference

### 💡 Example Usage
From Swagger UI:
1. Expand the endpoint (e.g. `POST /api/orders`)
2. Click **“Try it out”**
3. Enter a JSON body:
```json
{
  "customerName": "Ana Lilia Tiznado",
  "product": "Wireless Mouse",
  "quantity": 2,
  "price": 139.99,
  "orderDate": "2025-10-23T15:55:00"
}
```
4. Click Execute to run it directly from the browser.
