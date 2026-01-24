# Ecommerce Backend

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/aishaker/Ecommerce-backend)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A robust and scalable backend for an e-commerce platform built with Spring Boot. This project provides a comprehensive set of REST APIs for managing products, categories, users, shopping carts, orders, and payments.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Features

-   **User Management:** Secure user registration and authentication using JWT.
-   **Role-Based Access Control (RBAC):** Granular control over API access based on user roles and permissions.
-   **Product and Category Management:** Full CRUD operations for products and categories.
-   **Shopping Cart:** Persistent shopping cart functionality for users.
-   **Order Processing:** A complete workflow for order creation and management.
-   **Payment Integration:** Seamless payment processing with Stripe.
-   **Caching:** Redis for caching frequently accessed data to improve performance.
-   **Database Migration:** Flyway for version-controlled database schema management.
-   **API Documentation:** Interactive API documentation with Swagger UI.

## Technologies

-   **Framework:** Spring Boot 3.5.9
-   **Language:** Java 25
-   **Security:** Spring Security, JWT
-   **Database:** MySQL, Spring Data JPA, Flyway
-   **Caching:** Redis
-   **API Documentation:** SpringDoc (Swagger UI)
-   **Payment:** Stripe
-   **Utilities:** Lombok, MapStruct, Commons Codec

## Architecture

The application follows a classic layered architecture, promoting separation of concerns and maintainability.

-   **Controller Layer:** Exposes the REST APIs, handles HTTP requests, and performs request validation.
-   **Service Layer:** Implements the core business logic and orchestrates interactions between different components.
-   **Repository Layer:** Manages data access and persistence using Spring Data JPA.
-   **Domain Layer:** Consists of entities that model the application's data.
-   **DTO/Mapper Layers:** Facilitates data transfer between layers and maps between entities and DTOs.
-   **Common Module:** Contains shared utilities, configurations, and exception handlers.

## Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

-   Java 25
-   Gradle 8.x
-   MySQL 8.x
-   Redis

### Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/aishaker/Ecommerce-backend.git
    cd Ecommerce-backend/Ecommerce-backend
    ```

2.  **Set up the database:**
    -   Create a new MySQL database.
    -   Make sure your MySQL server is running.

3.  **Configure the application:**
    -   Set the required environment variables (see the [Configuration](#configuration) section). A common way to do this is to `export` them in your shell session.

4.  **Run the application:**
    ```bash
    ./gradlew bootRun
    ```

The application will be available at `http://localhost:8080`.

## Configuration

The application is configured through `application.yaml` files and environment variables. The `dev` profile is active by default for local development.

### Environment Variables

You need to set the following environment variables before running the application:

```bash
# Example for a Unix-like shell
export DB_URL="jdbc:mysql://localhost:3306/your_database_name"
export DB_USERNAME="your_mysql_username"
export DB_PASSWORD="your_mysql_password"

export REDIS_HOST="localhost"
export REDIS_PORT="6379"
export REDIS_PASSWORD="" # Set your Redis password if you have one

export JWT_SIGNING_KEY="your_super_secret_jwt_signing_key_that_is_long_and_secure"

export STRIPE_API_KEY="sk_test_your_stripe_api_key"
export STRIPE_SUCCESS_URL="http://localhost:8080/api/v1/payments/success"
export STRIPE_CANCEL_URL="http://localhost:8080/api/v1/payments/cancel"
export STRIPE_CURRENCY="usd"
```

-   `DB_URL`: The JDBC URL for your MySQL database.
-   `DB_USERNAME`: Your MySQL username.
-   `DB_PASSWORD`: Your MySQL password.
-   `REDIS_HOST`: The host for your Redis server.
-   `REDIS_PORT`: The port for your Redis server.
-   `REDIS_PASSWORD`: The password for your Redis server (if applicable).
-   `JWT_SIGNING_KEY`: A secret key for signing JWTs. This should be a long, random string.
-   `STRIPE_API_KEY`: Your secret API key for Stripe.
-   `STRIPE_SUCCESS_URL`: The URL to redirect to after a successful payment.
-   `STRIPE_CANCEL_URL`: The URL to redirect to after a canceled payment.
-   `STRIPE_CURRENCY`: The currency to use for Stripe payments.


### Application Profiles

The application uses Spring profiles to manage different environments. The following profiles are available:

-   `dev`: For local development (default).
-   `prod`: For production.

You can activate a profile by setting the `SPRING_PROFILES_ACTIVE` environment variable. For example:
```bash
export SPRING_PROFILES_ACTIVE=prod
```

## API Documentation

The REST APIs are documented using SpringDoc and Swagger UI. Once the application is running, you can access the interactive API documentation at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### API Endpoints

A summary of the available API endpoints:

#### Authentication API

| Method | Endpoint                      | Description              |
|--------|-------------------------------|--------------------------|
| POST   | /api/v1/auth/register         | Register a new user      |
| POST   | /api/v1/auth/login            | Authenticate a user      |
| POST   | /api/v1/auth/logout           | Log out a user           |
| POST   | /api/v1/auth/refresh-token    | Refresh a JWT            |

#### User API

| Method | Endpoint                          | Description              |
|--------|-----------------------------------|--------------------------|
| PUT    | /api/v1/users/{username}/profile  | Update a user's profile  |

#### Category Admin API

| Method | Endpoint                               | Description                       |
|--------|----------------------------------------|-----------------------------------|
| POST   | /api/v1/admin/categories               | Create a new category             |
| GET    | /api/v1/admin/categories/{id}          | Get a category by ID              |
| GET    | /api/v1/admin/categories               | Get a paginated list of categories|
| PUT    | /api/v1/admin/categories/{id}          | Update a category                 |
| PATCH  | /api/v1/admin/categories/{id}/status   | Activate or deactivate a category |
| DELETE | /api/v1/admin/categories/{id}          | Delete a category                 |

#### Product Admin API

| Method | Endpoint                                   | Description                               |
|--------|--------------------------------------------|-------------------------------------------|
| POST   | /api/v1/admin/products                     | Create a new product                      |
| GET    | /api/v1/admin/products/{id}                | Get a product by ID                       |
| GET    | /api/v1/admin/products                     | Get a paginated list of products          |
| GET    | /api/v1/admin/products/filter              | Get a filtered list of products           |
| PUT    | /api/v1/admin/products/{id}                | Update a product                          |
| PUT    | /api/v1/admin/products/update-category/{id}| Update a product's category               |
| PUT    | /api/v1/admin/products/{id}/status         | Activate or deactivate a product          |
| DELETE | /api/v1/admin/products/{id}                | Delete a product                          |
| PUT    | /api/v1/admin/products/{productId}/inventory| Update a product's stock                  |

#### Cart API

| Method | Endpoint                | Description                     |
|--------|-------------------------|---------------------------------|
| POST   | /api/v1/carts/{productId} | Add or update an item in the cart |
| GET    | /api/v1/carts           | View the cart for a given user  |

#### Payment API

| Method | Endpoint                      | Description                               |
|--------|-------------------------------|-------------------------------------------|
| POST   | /api/v1/payments/checkout     | Initiate checkout and get Stripe URL      |
| GET    | /api/v1/payments/success      | Handle successful payment callback        |
| GET    | /api/v1/payments/cancel       | Handle canceled payment callback          |

## Testing

To run the tests, execute the following command:

```bash
./gradlew test
```

## Contributing

Contributions are welcome! Please follow these steps to contribute:

1.  Fork the repository.
2.  Create a new branch (`git checkout -b feature/your-feature`).
3.  Make your changes.
4.  Commit your changes (`git commit -m 'Add some feature'`).
5.  Push to the branch (`git push origin feature/your-feature`).
6.  Open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.