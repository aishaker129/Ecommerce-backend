
CREATE TABLE payment_history (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                 order_id BIGINT NOT NULL,

                                 session_id VARCHAR(255) NOT NULL UNIQUE ,
                                 payment_intendent_id VARCHAR(255),

                                 payment_status VARCHAR(50) NOT NULL,

                                 created_at DATETIME NOT NULL,
                                 modified_at DATETIME,

    -- Foreign Key
                                 CONSTRAINT fk_payment_order
                                     FOREIGN KEY (order_id)
                                         REFERENCES orders(id)
                                         ON DELETE CASCADE
);