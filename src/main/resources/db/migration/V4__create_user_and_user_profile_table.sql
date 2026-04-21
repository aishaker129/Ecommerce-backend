-- ========================
-- USERS
-- ========================
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       email VARCHAR(50) NOT NULL UNIQUE,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       created_by BIGINT,
                       modified_by BIGINT
);

-- ========================
-- USER PROFILES
-- ========================
CREATE TABLE user_profiles (
                               id BIGSERIAL PRIMARY KEY,
                               user_id BIGINT NOT NULL UNIQUE,
                               phone_number VARCHAR(20),
                               date_of_birth DATE,
                               gender VARCHAR(10),
                               profile_image VARCHAR(1000),
                               image_public_id VARCHAR(1000),
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               modified_at TIMESTAMP,
                               CONSTRAINT fk_user_profiles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);