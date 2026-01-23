CREATE TABLE payment_history
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    order_id     BIGINT       NOT NULL,
    session_id   VARCHAR(255) NOT NULL,
    payment_link VARCHAR(255) NOT NULL,
    status       VARCHAR(255) NOT NULL,
    created_at   datetime     NOT NULL,
    modified_at  datetime NULL,
    CONSTRAINT pk_payment_history PRIMARY KEY (id)
);

ALTER TABLE payment_history
    ADD CONSTRAINT uc_payment_history_session UNIQUE (session_id);

ALTER TABLE payment_history
    ADD CONSTRAINT FK_PAYMENT_HISTORY_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);