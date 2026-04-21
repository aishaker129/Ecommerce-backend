-- ========================
-- ROLES
-- ========================
CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       code VARCHAR(25) NOT NULL UNIQUE
);

-- ========================
-- PERMISSIONS
-- ========================
CREATE TABLE permissions (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(100) NOT NULL,
                             code VARCHAR(50) NOT NULL UNIQUE
);

-- ========================
-- ROLE PERMISSIONS
-- ========================
CREATE TABLE role_permissions (
                                  role_id BIGINT NOT NULL,
                                  permission_id BIGINT NOT NULL,
                                  PRIMARY KEY (role_id, permission_id),
                                  CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
                                  CONSTRAINT fk_role_permissions_permission FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);