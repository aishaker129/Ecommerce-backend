-- V3__create_inventory_table.sql
CREATE TABLE inventories (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             product_id BIGINT NOT NULL UNIQUE,
                             total_quantity INT NOT NULL DEFAULT 0,
                             reserved_quantity INT NOT NULL DEFAULT 0,
                             version BIGINT NOT NULL DEFAULT 0,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             modified_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                             created_by BIGINT NULL,
                             modified_by BIGINT NULL,
                             CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
