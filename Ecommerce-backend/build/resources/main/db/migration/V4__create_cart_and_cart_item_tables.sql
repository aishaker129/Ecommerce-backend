
CREATE TABLE carts (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,

                       user_id BIGINT NOT NULL,

                       create_date DATETIME NULL,
                       modified_date DATETIME NULL
)ENGINE=InnoDB;


CREATE TABLE cart_items (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,

                            cart_id BIGINT NOT NULL,
                            product_id BIGINT NOT NULL,

                            quantity INT NOT NULL,
                            unit_price DOUBLE NOT NULL,

                            created_at TIMESTAMP,
                            updated_at TIMESTAMP,

                            CONSTRAINT fk_cart_items_cart
                                FOREIGN KEY (cart_id)
                                    REFERENCES carts(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_cart_items_product
                                FOREIGN KEY (product_id)
                                    REFERENCES products(id)
)ENGINE=InnoDB;
