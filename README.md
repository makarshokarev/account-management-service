# Account Management Service

A RESTful web service for managing account data with CRUD operations, phone number validation, and error handling.

## Technology Stack

- **Java 21** - Modern LTS version
- **Spring Boot 3.5** - Main framework
- **Spring Data JPA** - Database access
- **MySQL** - Database
- **SpringDoc OpenAPI** - API documentation
- **Liquibase** - Database migrations
- **JUnit 5 & Mockito** - Testing
- **Lombok** - Code generation
- **Docker Compose** - Container orchestration

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21**
- **Gradle 8.x** (or use included wrapper)
- **Docker Compose**

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/makarshokarev/account-management-service.git
cd account-management-service
```

### 2. Start Database

Start the MySQL database using Docker Compose:

```bash
docker compose up
```

### 3. Run Database Migrations

Execute database migrations:

```bash
./gradlew migrate
```

### 4. Start the Application

```bash
./gradlew bootRun
```

### 5. Access the Application

Once the application starts, verify it's working:

- **Application Health**: http://localhost:8080/api/v1/actuator/health
- **API Documentation**: http://localhost:8080/api/v1/swagger-ui.html

## Database Access

### MySQL Connection Settings

```yaml
url: jdbc:mysql://localhost:3306/account_db
username: root
password: password
```

## Logging

Application logs are saved to the `logs/` directory.

## Testing

### Run All Tests

```bash
# Execute all tests
./gradlew test

```
### Test Reports

- **Test Results**: `build/reports/tests/test/index.html`

## Monitoring and Health Checks

### Actuator Endpoints

| Endpoint | Description |
|----------|-------------|
| `/api/v1/actuator/health` | Application health status |
| `/api/v1/actuator/liquibase` | Database migration status |
