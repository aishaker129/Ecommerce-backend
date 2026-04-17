-- refresh token table

CREATE TABLE refresh_tokens (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                token VARCHAR(255) NOT NULL,
                                revoked BOOLEAN NOT NULL DEFAULT FALSE,

                                created_at DATETIME NOT NULL,
                                expiry_date DATETIME NOT NULL,
                                revoked_at DATETIME,

                                user_id BIGINT NOT NULL,

                                CONSTRAINT fk_refresh_tokens_user
                                    FOREIGN KEY (user_id) REFERENCES users(id)
                                        ON DELETE CASCADE
);