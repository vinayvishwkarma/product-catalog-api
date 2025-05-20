# Product Catalog API

## Overview

The **Product Catalog API** is a Spring Boot application that manages a catalog of products.  
It exposes RESTful endpoints to create, retrieve, update, and delete products, along with pagination
support.  
The application uses Redis for caching and is built using Maven.

## Features

- CRUD operations for products
- Pagination support for listing products
- Validation for product data
- Redis integration for caching
- OpenAPI documentation with Swagger UI
- Dockerized Redis setup via Docker Compose

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker and Docker Compose
- Redis

## Installation

### Clone the Repository

```bash
git clone <repository-url>
cd product-catalog-api
```

### Build the Application

```bash
./mvnw clean package
```

### Run Redis with Docker

```bash
docker-compose up -d
```

### Start the Application

```bash
./mvnw spring-boot:run
```

## Endpoints

### Product Endpoints

| Method | Endpoint                  | Description                          |
|--------|---------------------------|--------------------------------------|
| POST   | `/api/v1/products/create` | Create a new product                 |
| GET    | `/api/v1/products/{id}`   | Retrieve a product by ID            |
| PUT    | `/api/v1/products/{id}`   | Update a product by ID              |
| DELETE | `/api/v1/products/{id}`   | Delete a product by ID              |
| GET    | `/api/v1/products/list`   | List all products with pagination   |

### Health Check

| Method | Endpoint       | Description                          |
|--------|----------------|--------------------------------------|
| GET    | `/api/v1/ping` | Check database and Redis health      |

## Configuration

### `application.yml`

```yaml
spring:
  application:
    name: product-catalog-api
  cache:
    type: redis
  data:
    redis:
      host: localhost
      port: 6379
  springdoc:
    api-docs:
      enabled: true
      path: /v3/api-docs
      file: classpath:/openapi.yaml
    swagger-ui:
      enabled: true
      path: /swagger-ui.html
```

## OpenAPI Documentation

The API is documented using OpenAPI 3.0.  
Access the Swagger UI at:  
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Running Tests

Run unit tests using:

```bash
./mvnw test
```

## Docker Compose

The `docker-compose.yml` file is used to set up Redis. Start Redis with:

```bash
docker-compose up -d
```

## Project Structure

```
src/
├── main/
│   ├── java/com/learning/product_catalog_api/
│   │   ├── controller/    # REST controllers
│   │   ├── service/       # Business logic
│   │   ├── repository/    # Data access layer
│   │   ├── model/         # Entity classes
│   │   ├── util/          # Utility classes
│   │   └── config/        # Configuration classes
│   └── resources/
│       ├── application.yml
│       └── openapi.yaml   # OpenAPI documentation
└── test/
    └── java/com/learning/product_catalog_api/
        └── service/       # Unit tests
```

## License

This project is licensed under the Apache License 2.0. See the `LICENSE` file for details.
