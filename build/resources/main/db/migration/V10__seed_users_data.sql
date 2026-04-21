-- Seed users with BCrypt hashed password for 'password123'
-- Hash: $2a$10$DVaZleF3yUCi90ZV7FkVpOgYuq/ba8gawtr3lq8yFqUbKvhCYfhsW

INSERT INTO users (first_name, last_name, email, username, password,create_at)
VALUES ('Super', 'Admin', 'superadmin@example.com', 'superadmin',
        '$2a$12$E.X6XgJhkB7DvxxuPS3wK..WSyeiBuGqKZfw/F8upYO./MQ92PfAm', CURRENT_TIMESTAMP),
       ('Admin', 'User', 'admin@example.com', 'admin', '$2a$12$wflyBWbeW3MwQH4Pa.ARAejcRRyFflmW/rvQpjkTFM9H3E0.GFy9i',
        CURRENT_TIMESTAMP),
       ('Seller', 'User', 'seller@example.com', 'seller',
        '$2a$12$Gw8GwIZDWErFZ8./rZp4Xe8EwclasKZET8sprZzQgKShgJcBIjuwy', CURRENT_TIMESTAMP),
       ('Customer', 'User', 'customer@example.com', 'customer',
        '$2a$12$UmJk4WiHJzujuJrbJBApLekYdDMGfo.uSJKUEFV9GYN/YngkRYdh6', CURRENT_TIMESTAMP);

-- Map users to roles
INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM users WHERE username = 'superadmin'), (SELECT id FROM roles WHERE code = 'SUPER_ADMIN')),
       ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE code = 'ADMIN')),
       ((SELECT id FROM users WHERE username = 'seller'), (SELECT id FROM roles WHERE code = 'SELLER')),
       ((SELECT id FROM users WHERE username = 'customer'), (SELECT id FROM roles WHERE code = 'CUSTOMER'));

-- Create profile for customer role user only
INSERT INTO user_profiles (user_id, phone_number, date_of_birth, gender, created_at)
VALUES ((SELECT id FROM users WHERE username = 'customer'), '1234567890', '1990-01-01', 'MALE', CURRENT_TIMESTAMP);