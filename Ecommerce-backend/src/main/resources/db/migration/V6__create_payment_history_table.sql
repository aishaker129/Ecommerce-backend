
CREATE TABLE payment_history (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,

                                 order_id BIGINT NOT NULL,

                                 session_id VARCHAR(255) NOT NULL UNIQUE,
                                 payment_link VARCHAR(512) NOT NULL,

                                 status VARCHAR(50) NOT NULL,

                                 created_at DATETIME NOT NULL,
                                 modified_at DATETIME NULL,

                                 CONSTRAINT fk_payment_history_order
                                     FOREIGN KEY (order_id)
                                         REFERENCES orders(id)
                                         ON DELETE CASCADE
)ENGINE=InnoDB;
