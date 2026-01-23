-- V2__create_product_table.sql
CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          sku VARCHAR(100) NOT NULL UNIQUE,
                          name VARCHAR(200) NOT NULL,
                          description VARCHAR(1000),
                          price DOUBLE NOT NULL,
                          is_active BOOLEAN NOT NULL DEFAULT TRUE,
                          category_id BIGINT NOT NULL,
                          image_url VARCHAR(250),
                          created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                          modified_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                          created_by BIGINT NULL,
                          modified_by BIGINT NULL,
                          CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
