-- create user table

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,

                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       email VARCHAR(50) NOT NULL UNIQUE,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,

                       create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                       created_by BIGINT,
                       modified_by BIGINT
);

CREATE TABLE user_profiles (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,

                               user_id BIGINT NOT NULL UNIQUE,

                               phone_number VARCHAR(20),
                               date_of_birth DATE,
                               gender VARCHAR(10),

                               created_at DATETIME NOT NULL,
                               modified_at DATETIME,

                               CONSTRAINT fk_user_profiles_user
                                   FOREIGN KEY (user_id) REFERENCES users(id)
                                       ON DELETE CASCADE
);