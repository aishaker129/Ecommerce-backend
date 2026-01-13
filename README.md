# Ecommerce Backend

> A focused, production-style Spring Boot backend for e-commerce: product catalog, inventory, cart, order, and Stripe-based payment flow.

---

## Table of contents
- [Project overview](#project-overview)
- [Run & develop locally](#run--develop-locally)
- [Configuration (DB, Stripe, JWT)](#configuration-db-stripe-jwt)
- [Database & migrations](#database--migrations)
- [Key modules & APIs](#key-modules--apis)
- [Known issues & TODOs](#known-issues--todos)
- [Testing](#testing)
- [Security guidance](#security-guidance)
- [How I can help next](#how-i-can-help-next)

---

## Project overview
This repository contains a backend service built with Spring Boot 3.x that implements core e-commerce features:
- Product management (admin endpoints)
- Inventory management
- Shopping cart (per-user)
- Order creation and lifecycle
- Payment integration using Stripe (checkout sessions + success callback)

Code structure highlights:
- Main app: `src/main/java/com/ecommerce` (domain packages: `product`, `cart`, `order`, `payment`, `common`)
- DB migrations: `src/main/resources/db/migration` (Flyway SQL files)
- Configuration: `src/main/resources/application-*.yaml`

---

## Run & develop locally
Prereqs:
- Java 25 (project toolchain)
- MySQL (or change datasource to another DB)
- Git

From repository root:

```bash
# go to the Spring Boot project directory
cd Ecommerce-backend
# run the app
./gradlew bootRun
# run tests
./gradlew test
```

Notes:
- The Gradle wrapper is in the `Ecommerce-backend` subfolder.
- App starts on port 8080 by default.
- OpenAPI (Swagger) UI is available when running: http://localhost:8080/swagger-ui/index.html

---

## Configuration (DB, Stripe, JWT)
The project currently reads configuration from `application-dev.yaml` (dev profile). **Do not commit secrets**. Instead:
- Use environment variables or a local `application-local.yaml` (gitignored).
- Consider `spring.config.import` for environment-specific secrets or an external vault.

Important properties used in code:
- Datasource: `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`
- Flyway: `spring.flyway.locations`
- JWT (example): `application.security.jwt.signing-key`
- Stripe: `stripe.api-key`, `stripe.success-url`, `stripe.cancel-url`, `stripe.currency`

Recommended env vars (example):
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ecommerce_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=secret
APPLICATION_SECURITY_JWT_SIGNING_KEY=replace_with_secure_value
STRIPE_API_KEY=sk_test_...
STRIPE_SUCCESS_URL=http://localhost:8080/api/v1/payment/success?sessionId={CHECKOUT_SESSION_ID}
STRIPE_CANCEL_URL=http://localhost:8080/api/v1/payment/cancel
STRIPE_CURRENCY=bdt
```

---

## Database & migrations
- Flyway is configured and runs on application startup using SQL files in `classpath:db/migration`.
- To create a local DB quickly using Docker:

```bash
docker run --name ecommerce-db -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=ecommerce_db -p 3306:3306 -d mysql:8.0
```

Then start the app; Flyway will apply migrations (V1..V6 present in `src/main/resources/db/migration`).

---

## Key modules & APIs
Below are the most important endpoints and their purpose (use the Swagger UI or OpenAPI for full request/response schemas).

Product Admin (admin scope):
- Base: `POST /api/v1/admin/products` — create product
- `GET /api/v1/admin/products/{id}` — get product by id
- `GET /api/v1/admin/products` — list products (pagination)
- `GET /api/v1/admin/products/filter` — filter by category/status/price range
- `PUT /api/v1/admin/products/{id}` — update product
- `PUT /api/v1/admin/products/update-category/{id}` — change product category
- `PUT /api/v1/admin/products/{id}/status?isActive=true|false` — toggle active
- `DELETE /api/v1/admin/products/{id}` — hard delete
- `PUT /api/v1/admin/products/{productId}/inventory` — update inventory for a product

Cart:
- `POST /api/v1/cart/items/{productId}` — add or update cart item (quantity 0 removes it)
- `GET  /api/v1/cart?user_Id={userId}` — view user's cart

Payment:
- `POST /api/v1/payment/checkout?user_Id={userId}` — create Stripe checkout session for the user's cart
  - Response contains `checkoutUrl` (Stripe hosted payment page)
- `GET  /api/v1/payment/success?sessionId={sessionId}` — callback endpoint to finalize successful payments (updates order status and inventory)

Notes on flow:
1. Checkout reserves stock via `InventoryService.checkAndReserveStock`.
2. Order is created from the cart (`OrderService.createOrderFromCart`).
3. Cart is cleared, Stripe session is created and `PaymentHistory` entry is stored.
4. On success callback, payment history is updated and reserved stock is finalized.

---
