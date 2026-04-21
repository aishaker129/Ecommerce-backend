-- ========================
-- CATEGORIES
-- ========================
CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            category_name VARCHAR(120) NOT NULL,
                            category_code VARCHAR(50) NOT NULL,
                            is_active BOOLEAN DEFAULT TRUE,
                            create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            created_by BIGINT,
                            modified_by BIGINT
);