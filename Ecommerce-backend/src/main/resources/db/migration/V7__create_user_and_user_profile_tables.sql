CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,

                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(50),
                       email VARCHAR(100) NOT NULL UNIQUE,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,

                       created_at DATETIME  NULL,
                       updated_at DATETIME NULL,
                       created_by DATETIME NULL,
                       modified_by DATETIME NULL,

                       INDEX idx_users_email (email),
                       INDEX idx_users_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_profiles (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,

                               user_id BIGINT UNIQUE,
                               phone_number VARCHAR(20),
                               date_of_birth DATE,
                               gender VARCHAR(10),

                               created_at DATETIME NULL,
                               modified_at DATETIME,

                               CONSTRAINT fk_user_profiles_user
                                   FOREIGN KEY (user_id) REFERENCES users (id)
                                       ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
