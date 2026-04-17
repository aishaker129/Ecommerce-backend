-- create inventories table

CREATE TABLE inventories(
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            product_id BIGINT NOT NULL UNIQUE ,
                            total_quantity INT NOT NULL DEFAULT 0,
                            reserved_quantity INT NOT NULL DEFAULT 0,
                            version BIGINT NOT NULL DEFAULT 0,
                            create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            created_by BIGINT,
                            modified_by BIGINT,
                            CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES products(id)
);