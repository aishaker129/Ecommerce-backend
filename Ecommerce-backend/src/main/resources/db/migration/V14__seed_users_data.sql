-- Seed users with BCrypt hashed password for 'password123'
-- Hash: $2a$10$DVaZleF3yUCi90ZV7FkVpOgYuq/ba8gawtr3lq8yFqUbKvhCYfhsW
INSERT INTO users (first_name, last_name, email, username, password, created_at)
SELECT 'Super', 'Admin', 'superadmin@example.com', 'superadmin',
       '$2a$10$DVaZleF3yUCi90ZV7FkVpOgYuq/ba8gawtr3lq8yFqUbKvhCYfhsW',
       CURRENT_TIMESTAMP
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'superadmin'
);

INSERT INTO users (first_name, last_name, email, username, password, created_at)
SELECT 'Admin', 'User', 'admin@example.com', 'admin',
       '$2a$10$DVaZleF3yUCi90ZV7FkVpOgYuq/ba8gawtr3lq8yFqUbKvhCYfhsW',
       CURRENT_TIMESTAMP
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);

INSERT INTO users (first_name, last_name, email, username, password, created_at)
SELECT 'Seller', 'User', 'seller@example.com', 'seller',
       '$2a$10$DVaZleF3yUCi90ZV7FkVpOgYuq/ba8gawtr3lq8yFqUbKvhCYfhsW',
       CURRENT_TIMESTAMP
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'seller'
);

INSERT INTO users (first_name, last_name, email, username, password, created_at)
SELECT 'Customer', 'User', 'customer@example.com', 'customer',
       '$2a$10$DVaZleF3yUCi90ZV7FkVpOgYuq/ba8gawtr3lq8yFqUbKvhCYfhsW',
       CURRENT_TIMESTAMP
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'customer'
);

-- Map users to roles
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.code = 'SUPER_ADMIN'
WHERE u.username = 'superadmin'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur
    WHERE ur.user_id = u.id AND ur.role_id = r.id
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.code = 'ADMIN'
WHERE u.username = 'admin'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur
    WHERE ur.user_id = u.id AND ur.role_id = r.id
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.code = 'SELLER'
WHERE u.username = 'seller'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur
    WHERE ur.user_id = u.id AND ur.role_id = r.id
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.code = 'CUSTOMER'
WHERE u.username = 'customer'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur
    WHERE ur.user_id = u.id AND ur.role_id = r.id
);

-- Create profile for customer role user only
INSERT INTO user_profiles (user_id, phone_number, date_of_birth, gender, created_at)
SELECT u.id, '1234567890', '1990-01-01', 'MALE', CURRENT_TIMESTAMP
FROM users u
WHERE u.username = 'customer'
  AND NOT EXISTS (
    SELECT 1 FROM user_profiles up WHERE up.user_id = u.id
);
