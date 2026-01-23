
CREATE TABLE orders (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,

                        user_id BIGINT NOT NULL,

                        sub_total DOUBLE NOT NULL,
                        discount_amount DOUBLE NOT NULL,
                        delivery_charge DOUBLE NOT NULL,
                        total_price DOUBLE NOT NULL,

                        status VARCHAR(50) NOT NULL,

                        create_date DATETIME  NULL
)ENGINE=InnoDB;

CREATE TABLE order_items (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,

                             order_id BIGINT NOT NULL,

                             product_id BIGINT NOT NULL,
                             product_name VARCHAR(255) NOT NULL,
                             product_sku VARCHAR(100) NOT NULL,

                             quantity INT NOT NULL,
                             unit_price DOUBLE NOT NULL,

                             CONSTRAINT fk_order_items_order
                                 FOREIGN KEY (order_id)
                                     REFERENCES orders(id)
                                     ON DELETE CASCADE
)ENGINE=InnoDB;

