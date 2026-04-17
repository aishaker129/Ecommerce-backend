-- create product table

CREATE TABLE products(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    sku VARCHAR(50) NOT NULL UNIQUE ,
    product_description VARCHAR(255),
    product_price DOUBLE  NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    product_image VARCHAR(500) NOT NULL ,
    category_id BIGINT NOT NULL ,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    modified_by BIGINT,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id)
);