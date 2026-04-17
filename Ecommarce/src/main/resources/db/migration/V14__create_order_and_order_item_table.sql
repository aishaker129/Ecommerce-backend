
CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,

                        user_id BIGINT NOT NULL,

                        subtotal DOUBLE NOT NULL,
                        discount_amount DOUBLE NOT NULL,
                        delivery_charge DOUBLE NOT NULL,
                        total_price DOUBLE NOT NULL,

                        status VARCHAR(50) NOT NULL,

                        created_at DATETIME,

    -- Foreign Key
                        CONSTRAINT fk_orders_user
                            FOREIGN KEY (user_id)
                                REFERENCES users(id)
                                ON DELETE CASCADE
);

CREATE TABLE order_items (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,

                             order_id BIGINT NOT NULL,
                             product_id BIGINT,

                             product_name VARCHAR(255) NOT NULL,
                             sku VARCHAR(100),

                             quantity INT NOT NULL,
                             unit_price DOUBLE NOT NULL,

    -- Foreign Keys
                             CONSTRAINT fk_order_items_order
                                 FOREIGN KEY (order_id)
                                     REFERENCES orders(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_order_items_product
                                 FOREIGN KEY (product_id)
                                     REFERENCES products(id)
                                     ON DELETE SET NULL
);