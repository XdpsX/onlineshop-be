-- liquibase formatted sql

-- changeset xdpsx:issue-9
-- comment: Update categories table
ALTER TABLE categories
DROP COLUMN slug;

ALTER TABLE categories
MODIFY COLUMN created_at TIMESTAMP NOT NULL,
MODIFY COLUMN updated_at TIMESTAMP;

ALTER TABLE categories
ADD COLUMN image VARCHAR(255) AFTER name,
ADD COLUMN public_flg BOOLEAN NOT NULL DEFAULT FALSE AFTER image,
ADD COLUMN parent_id INT AFTER public_flg,
ADD CONSTRAINT fk_parent_category FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL;