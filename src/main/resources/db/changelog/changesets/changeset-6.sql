-- liquibase formatted sql

-- changeset xdpsx:issue-27
-- comment: Update users table
ALTER TABLE users
DROP COLUMN avatar,
DROP COLUMN role;

ALTER TABLE users
MODIFY COLUMN password VARCHAR(255) NOT NULL;

ALTER TABLE users
ADD COLUMN avatar_id VARCHAR(30) AFTER password,
ADD COLUMN phone_number VARCHAR(15) AFTER avatar_id,
ADD COLUMN enabled BOOLEAN NOT NULL DEFAULT FALSE AFTER phone_number,
ADD COLUMN locked BOOLEAN NOT NULL DEFAULT FALSE AFTER enabled,
ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP AFTER locked,
ADD COLUMN updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_at,
ADD CONSTRAINT fk_user_media FOREIGN KEY (avatar_id) REFERENCES media(id) ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS users_roles (
    user_id BIGINT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
