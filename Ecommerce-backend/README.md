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
    -   Update the database credentials in `src/main/resources/application-dev.yaml`.

3.  **Configure the application:**
    -   Create a `.env` file in the `Ecommerce-backend` directory.
    -   Add the required environment variables to the `.env` file (see the [Configuration](#configuration) section).

4.  **Run the application:**
    ```bash
    ./gradlew bootRun
    ```

The application will be available at `http://localhost:8080`.

## Configuration

The application is configured through `application.yaml` files and environment variables.

### Environment Variables

Create a `.env` file in the `Ecommerce-backend` directory and add the following variables:

```
STRIPE_SECRET_KEY=your_stripe_secret_key
JWT_SECRET=your_jwt_secret
```

-   `STRIPE_SECRET_KEY`: Your secret key for the Stripe API.
-   `JWT_SECRET`: The secret key for signing JWTs.

### Application Profiles

The application uses Spring profiles to manage different environments. The following profiles are available:

-   `dev`: For local development.
-   `prod`: For production.

You can activate a profile by setting the `SPRING_PROFILES_ACTIVE` environment variable.

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