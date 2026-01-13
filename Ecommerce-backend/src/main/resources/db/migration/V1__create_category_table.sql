-- V1__create_category_table.sql
CREATE TABLE categories (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(120) NOT NULL,
                            code VARCHAR(50) NOT NULL UNIQUE,
                            is_active BOOLEAN NOT NULL DEFAULT TRUE,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            modified_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                            created_by BIGINT NULL,
                            modified_by BIGINT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
