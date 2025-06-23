# Ecommerce Inventory Management System

This is a Spring Boot-based inventory management backend for an ecommerce system. It supports real-time item reservation, stock tracking, and caching via Redis. PostgreSQL is used for persistence, and H2 is used for unit testing.

---

##  Features

- Add, update, delete items in inventory
- Reserve and cancel reservations on items
- Track available quantity
- Redis caching for performance
- PostgreSQL as the production database
- H2 for testing
- REST APIs documented via Swagger

---

## Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL
- Redis (Memurai for Windows or standard Redis)
- Spring Cache
- Swagger / OpenAPI
- JUnit + Mockito
- Maven or Gradle

---

## ⚙️ Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/<your-org>/ecommerce-inventory-management.git
cd ecommerce-inventory-management

Spring Boot-based e-commerce inventory system with Redis caching and PostgreSQL

**2. Configure database & Redis**

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5433/ecommerce_inventory
spring.datasource.username=postgres
spring.datasource.password=1234
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Redis
spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379

# Server
server.port=8081

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true


**3. Run the application**

# Maven
./mvnw spring-boot:run


**4. Access API docs**
Open: http://localhost:8081/swagger-ui.html

**Running Tests**

# Maven
./mvnw test


