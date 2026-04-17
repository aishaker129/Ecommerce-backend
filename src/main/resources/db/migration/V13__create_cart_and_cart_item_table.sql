
-- create cart table

CREATE TABLE carts (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,

                       user_id BIGINT NOT NULL UNIQUE,

                       created_at DATETIME,
                       modified_at DATETIME,

    -- Foreign Key Constraint
                       CONSTRAINT fk_cart_user
                           FOREIGN KEY (user_id)
                               REFERENCES users(id)
                               ON DELETE CASCADE
);

-- cart item table

CREATE TABLE cart_items (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,

                            cart_id BIGINT NOT NULL,
                            product_id BIGINT NOT NULL,

                            quantity INT NOT NULL,
                            unit_price DECIMAL(10,2) NOT NULL,

                            created_at DATETIME,
                            modified_at DATETIME,

    -- Unique constraint (one product per cart)
                            CONSTRAINT uk_cart_product UNIQUE (cart_id, product_id),

    -- Foreign keys
                            CONSTRAINT fk_cart_item_cart
                                FOREIGN KEY (cart_id)
                                    REFERENCES carts(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_cart_item_product
                                FOREIGN KEY (product_id)
                                    REFERENCES products(id)
);