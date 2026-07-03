# ATM Transaction System — Spring Boot Microservices

## 1. Project Overview

### Project Description
A microservices-based ATM Transaction System that manages user authentication, customer records, bank accounts, and financial transactions (deposit, withdraw, transfer) across independently deployable Spring Boot services, coordinated through an API Gateway and Eureka service discovery.

### Features
- JWT-based authentication and authorization
- Customer and account management
- Deposit, withdrawal, and fund transfer operations
- Centralized transaction history and audit trail
- Service discovery via Eureka
- Centralized routing and security enforcement via API Gateway
- Structured, consistent JSON responses across all services
- Interactive API documentation via Swagger UI (aggregated at the Gateway)

### Technologies Used
- Java 21
- Spring Boot 4.1.0
- Spring Security 7.1.0
- Spring Cloud 2025.1.2 (Gateway Server WebMVC, Eureka, LoadBalancer, OpenFeign)
- JWT (JJWT 0.12.7)
- MySQL
- Hibernate / Spring Data JPA
- Maven
- Springdoc OpenAPI (Swagger UI)
- Postman

---

## 2. Architecture

```
Client
   │
   ▼
API Gateway (8080)
   │
   ├──────────────┐
   │              │
   ▼              ▼
Auth Service    Customer Service
   │              │
   └──────┐       │
          ▼       ▼
      Account Service
             │
             ▼
     Transaction Service
             │
             ▼
          MySQL Databases
```

All client traffic enters through the API Gateway, which authenticates requests via JWT and routes them to the appropriate downstream service using Eureka-based service discovery. Transaction Service is the single owner of all balance-changing operations (deposit, withdraw, transfer) and communicates with Account Service internally via OpenFeign — Account Service's own deposit/withdraw endpoints are not exposed externally through the Gateway.

---

## 3. Microservices

| Service | Port | Responsibility |
|---|---|---|
| Eureka Server | 8761 | Service registry and discovery |
| API Gateway | 8080 | Routing, JWT validation, single entry point |
| Auth Service | 8081 | User registration, login, JWT issuance |
| Customer Service | 8082 | Customer record management |
| Account Service | 8083 | Account creation, balance storage |
| Transaction Service | 8084 | Deposit, withdraw, transfer, transaction history |

---

## 4. Database Setup

Create the following databases before starting the services:

```sql
CREATE DATABASE bank_auth;
CREATE DATABASE bank_customer;
CREATE DATABASE bank_account;
CREATE DATABASE bank_transaction;
```

**Credentials used by each service:**
```
Username: root
Password: root
```

> Update `spring.datasource.username` / `spring.datasource.password` in each service's `application.properties` if your local MySQL credentials differ.

---

## 5. Service Startup Order

Services must be started in this order to register correctly with Eureka and resolve dependencies:

```
1. Eureka Server        (8761)
2. Auth Service         (8081)
3. Customer Service     (8082)
4. Account Service      (8083)
5. Transaction Service  (8084)
6. API Gateway          (8080)
```

---

## 6. Verify Eureka

Open:
```
http://localhost:8761
```

You should see all five services registered and `UP`:
```
AUTH-SERVICE
CUSTOMER-SERVICE
ACCOUNT-SERVICE
TRANSACTION-SERVICE
API-GATEWAY
```

---

## 7. API Testing Order

All requests go through the Gateway on port **8080**.

### Register
```
POST http://localhost:8080/auth/register
```
```json
{
  "username": "hema",
  "password": "admin123",
  "role": "ADMIN"
}
```

### Login
```
POST http://localhost:8080/auth/login
```
```json
{
  "username": "hema",
  "password": "admin123"
}
```
Copy the returned JWT and use it as a `Bearer` token in the `Authorization` header for all subsequent requests.

### Create Customer
```
POST http://localhost:8080/customers
```

### Get Customer
```
GET http://localhost:8080/customers/1
```

### Create Account
```
POST http://localhost:8080/accounts
```

### Search Account
```
GET http://localhost:8080/accounts/{accountNumber}
```

### Deposit
```
POST http://localhost:8080/transactions/deposit
```

### Withdraw
```
POST http://localhost:8080/transactions/withdraw
```

### Check Balance
```
GET http://localhost:8080/accounts/balance/{accountNumber}
```

### Transfer
```
POST http://localhost:8080/transactions/transfer
```

### Transaction History
```
GET http://localhost:8080/transactions/{accountNumber}
```

> **Note:** `POST /accounts/deposit` and `POST /accounts/withdraw` are intentionally blocked at the Gateway for external clients. All deposits, withdrawals, and transfers must go through Transaction Service, which is the single owner of these operations and records every transaction. Account Service's endpoints are only reachable internally via OpenFeign.

---

## 8. Database Tables

| Database | Table |
|---|---|
| `bank_auth` | `users` |
| `bank_customer` | `customers` |
| `bank_account` | `accounts` |
| `bank_transaction` | `transactions` |

---

## 9. Exception Handling

All services return structured, consistent error responses in this format:

```json
{
  "success": false,
  "status": 404,
  "error": "ACCOUNT_NOT_FOUND",
  "message": "Account 1071542357 does not exist.",
  "timestamp": "2026-07-03T12:30:20",
  "path": "/accounts/1071542357"
}
```

Handled cases include:

- **Account Not Found** — requested account number does not exist in Account Service
- **Customer Not Found** — requested customer ID does not exist in Customer Service
- **Insufficient Balance** — withdrawal or transfer amount exceeds available balance
- **Invalid Credentials** — login attempted with incorrect username/password
- **Duplicate Username** — registration attempted with a username that already exists

Each service implements a `GlobalExceptionHandler` (`@RestControllerAdvice`) that intercepts exceptions and converts them into this structured format, rather than returning Spring's default error page.

---

## 10. JWT Authentication

```
Register
   │
   ▼
Login
   │
   ▼
Receive JWT
   │
   ▼
Use Bearer Token
   │
   ▼
Access Protected APIs
```

- Auth Service issues a signed JWT on successful login, containing the username as the subject and an expiration claim.
- The API Gateway's `JwtAuthenticationFilter` validates the token on every request (except `/auth/**` and documentation endpoints), and on success populates the Spring Security context so downstream authorization checks succeed.
- All protected endpoints require the header:
  ```
  Authorization: Bearer <token>
  ```

---

## 11. OpenFeign — Service-to-Service Communication

```
Transaction Service
        │
        ▼
   Feign Client
        │
        ▼
  Account Service
        │
        ▼
  Balance Updated
        │
        ▼
   Return Result
```

Transaction Service uses a declarative OpenFeign client (`AccountClient`) to call Account Service directly via Eureka service discovery — this traffic never passes through the API Gateway. This keeps Account Service as the single source of truth for balances while Transaction Service owns the business logic and audit trail for every deposit, withdrawal, and transfer, including compensating rollback if a transfer's deposit leg fails after its withdrawal leg has already succeeded.

---

## 12. API Documentation (Swagger)

All service APIs are documented via Springdoc OpenAPI and aggregated into a single Swagger UI at the Gateway:

```
http://localhost:8080/swagger-ui/index.html
```

Use the dropdown in the top-right corner to switch between Auth, Customer, Account, and Transaction service documentation.

---

## 13. Future Improvements

- Docker containerization for all services
- Kubernetes orchestration and deployment manifests
- Redis for distributed caching (e.g., account balance lookups)
- Kafka or RabbitMQ for asynchronous transaction event processing
- Centralized monitoring and observability (e.g., Prometheus, Grafana, distributed tracing)
- CI/CD pipeline for automated build, test, and deployment
- Full saga-pattern implementation for distributed transaction consistency across services

---

## 14. Postman Collection

A ready-to-import Postman collection (`ATM-Transaction-System.postman_collection.json`) is included, organized as:

```
ATM Transaction System
│
├── Authentication
│      Register
│      Login          (auto-saves JWT to a collection variable)
│
├── Customer
│      Create Customer
│      Get Customer
│
├── Account
│      Create Account
│      Search Account
│      Check Balance
│
├── Transactions
│      Deposit
│      Withdraw
│      Transfer
│      Transaction History
```

The **Login** request includes a test script that automatically extracts the JWT from the response and stores it in a collection variable (`jwt_token`). Every other protected request references `{{jwt_token}}` in its Authorization header, so once you run Login, all subsequent requests are authenticated automatically — no manual copy-pasting required.
