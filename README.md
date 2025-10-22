# MELI Order System

## 📁 Project Structure

```
CH5-Technoready/
│
├── meli/
│   ├── .mvn/                      # Maven wrapper files
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/              # Java source code
│   │   │   └── resources/         # Configuration files (application.properties)
│   │   └── test/                  # Unit and integration tests
│   ├── target/                    # Compiled output (auto-generated)
│   ├── mvnw / mvnw.cmd            # Maven wrapper executables
│   ├── pom.xml                    # Maven project configuration
│   └── README.md                  # Project documentation
│
└── .gitignore                     # Git ignored files and directories
```

---

## ⚙️ Technologies Used

- **Java 17**
- **Spring Boot 3.3.3**
    - Spring Web
    - Spring Data JPA
    - Spring Validation
- **H2 Database (in-memory)**
- **Maven**
- **Lombok** (for boilerplate reduction)

---

## 🚀 How to Run the Project

### Prerequisites
- Java 17 or higher
- Maven installed or use Maven Wrapper (`mvnw`)

### Run Commands
#### Option 1 — Using Maven Wrapper
```bash
./mvnw spring-boot:run
```

#### Option 2 — Using Maven Installed Globally
```bash
mvn spring-boot:run
```

Once started:
- API Base URL → `http://localhost:8080`
- H2 Console → `http://localhost:8080/h2-console`  
  **JDBC URL:** `jdbc:h2:mem:testdb`  
  **Username:** `sa`  
  **Password:** *(leave blank)*

---

## 🧩 Available Endpoints

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `POST` | `/api/orders` | Create a new order |
| `GET` | `/api/orders` | List all orders |
| `GET` | `/api/orders/{id}` | Retrieve an order by ID |
| `PUT` | `/api/orders/{id}` | Update an existing order |
| `DELETE` | `/api/orders/{id}` | Delete an order |

### Example: Create Order (POST)
```json
{
  "customerName": "Carlos Jiménez",
  "product": "Noise Cancelling Headphones",
  "quantity": 2,
  "price": 1999,
  "orderDate": "2025-10-17T14:05:00"
}
```
