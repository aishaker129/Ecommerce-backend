# Ecommerce Backend

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/aishaker/Ecommerce-backend)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A robust and scalable backend for **Ecommerce**, an e-commerce platform built with Spring Boot. This project provides a comprehensive set of REST APIs for managing products, categories, users, shopping carts, orders, and payments.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Docker Setup](#docker-setup)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Features

-   **User Management:** Secure user registration, profile management, and authentication using JWT.
-   **Role-Based Access Control (RBAC):** Granular control over API access based on user roles and permissions.
-   **Product and Category Management:** Full CRUD operations for products and categories, including inventory tracking.
-   **Image Upload:** Integration with **Cloudinary** for efficient product and profile image management.
-   **Shopping Cart:** Persistent shopping cart functionality for users.
-   **Order Processing:** A complete workflow for order creation, viewing, and management.
-   **Payment Integration:** Seamless payment processing with **Stripe**, including webhook support for payment confirmation.
-   **Caching:** **Redis** for caching frequently accessed data to improve performance.
-   **Database Migration:** **Flyway** for version-controlled database schema management.
-   **API Documentation:** Interactive API documentation with **Swagger UI**.

## Technologies

-   **Framework:** Spring Boot 3.4.4
-   **Language:** Java 21
-   **Security:** Spring Security, JWT (jjwt)
-   **Database:** PostgreSQL, Spring Data JPA, Flyway
-   **Caching:** Redis
-   **Image Storage:** Cloudinary
-   **API Documentation:** SpringDoc (Swagger UI 2.8.6)
-   **Payment:** Stripe
-   **Utilities:** Lombok, MapStruct

## Architecture

The application follows a classic layered architecture, promoting separation of concerns and maintainability.

-   **Controller Layer:** Exposes the REST APIs, handles HTTP requests, and performs request validation.
-   **Service Layer:** Implements the core business logic and orchestrates interactions between different components.
-   **Repository Layer:** Manages data access and persistence using Spring Data JPA.
-   **Domain Layer:** Consists of entities that model the application's data.
-   **DTO/Mapper Layers:** Facilitates data transfer between layers and maps between entities and DTOs.
-   **Common Module:** Contains shared utilities, constants (e.g., `ApiEndPoints`), and exception handlers.

## Getting Started

### Prerequisites

-   **Java 21**
-   **Gradle 8.x**
-   **PostgreSQL**
-   **Redis**

### Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/aishaker/Ecommerce-backend.git
    cd Ecommerce-backend
    ```

2.  **Set up the database:**
    -   Create a new PostgreSQL database.
    -   Ensure your PostgreSQL server is running.

3.  **Configure the application:**
    -   Set the required environment variables (see the [Configuration](#configuration) section).

4.  **Run the application:**
    ```bash
    ./gradlew bootRun
    ```

The application will be available at `https://ecommerce-backend-tykm.onrender.com/` or `http://localhost:8081`.

## Configuration

The application is configured through `application.yaml` and environment variables.

### Environment Variables

You need to set the following environment variables:

```bash
# Database Configuration
export DB_URL="jdbc:postgresql://localhost:5432/campuskart"
export DB_USERNAME="your_username"
export DB_PASSWORD="your_password"

# Redis Configuration
export REDIS_HOST="localhost"
export REDIS_PORT="6379"
export REDIS_PASSWORD=""

# Security Configuration
export JWT_SIGNING_KEY="your_secure_jwt_signing_key"

# Cloudinary Configuration
export CLOUD_NAME="your_cloud_name"
export CLOUDINARY_API_KEY="your_api_key"
export CLOUDINARY_API_SECRET="your_api_secret"

# Stripe Configuration
export STRIPE_SECRET_KEY="sk_test_..."
export WEBHOOK_SECRET_KEY="whsec_..."
export STRIPE_SUCCESS_URL="https://ecommerce-backend-tykm.onrender.com/api/v1/payment/success"
export STRIPE_CANCEL_URL="https://ecommerce-backend-tykm.onrender.com/api/v1/payment/cancel"
```

## API Documentation

Access the interactive Swagger UI at:
[https://ecommerce-backend-tykm.onrender.com/swagger-ui.html](https://ecommerce-backend-tykm.onrender.com/swagger-ui.html)

### Key API Endpoints

#### Authentication
- `POST /api/v1/auth/register` - Register a new user
- `POST /api/v1/auth/login` - Authenticate and get tokens
- `POST /api/v1/auth/refresh` - Refresh access token
- `POST /api/v1/auth/logout` - Logout user

#### Products & Categories (Admin)
- `POST /api/v1/products/add-product` - Add new product
- `GET /api/v1/products/product-list` - List all products
- `PUT /api/v1/products/{id}/update-product` - Update product details
- `POST /api/v1/categories/add-category` - Add new category
- `GET /api/v1/categories/view-category` - View categories

#### Cart & Orders
- `POST /api/v1/cart/add-to-cart` - Add item to cart
- `GET /api/v1/cart/carts/{userId}` - View user's cart
- `POST /api/v1/order/create` - Create a new order
- `GET /api/v1/order/user/{userId}` - View user's orders

#### Payments
- `POST /api/v1/payment/checkout` - Initiate Stripe checkout
- `POST /api/v1/webhook/stripe` - Stripe webhook listener

## Docker Setup

To run the application and Redis using Docker Compose:

1.  **Build and start:**
    ```bash
    docker-compose up --build
    ```
2.  The application will be accessible at `http://localhost:8081`.

## Testing

Run tests using Gradle:
```bash
./gradlew test
```

## Contributing

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Push to the branch.
5. Open a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
