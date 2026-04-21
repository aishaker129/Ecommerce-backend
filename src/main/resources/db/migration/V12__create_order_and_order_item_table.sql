-- ========================
-- ORDERS
-- ========================
CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        subtotal DOUBLE PRECISION NOT NULL,
                        discount_amount DOUBLE PRECISION NOT NULL,
                        delivery_charge DOUBLE PRECISION NOT NULL,
                        total_price DOUBLE PRECISION NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ========================
-- ORDER ITEMS
-- ========================
CREATE TABLE order_items (
                             id BIGSERIAL PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             product_id BIGINT,
                             product_name VARCHAR(255) NOT NULL,
                             sku VARCHAR(100),
                             quantity INT NOT NULL,
                             unit_price DOUBLE PRECISION NOT NULL,
                             CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                             CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL
);