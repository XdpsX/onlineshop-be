-- liquibase formatted sql

-- changeset xdpsx:issue-9
-- comment: Update categories table
ALTER TABLE categories
DROP COLUMN slug;

ALTER TABLE categories
MODIFY COLUMN created_at TIMESTAMP NOT NULL,
MODIFY COLUMN updated_at TIMESTAMP;

ALTER TABLE categories
ADD COLUMN public_flg BOOLEAN NOT NULL DEFAULT FALSE AFTER name,
ADD COLUMN image_id VARCHAR(30) AFTER public_flg,
ADD COLUMN parent_id INT AFTER image_id,
ADD CONSTRAINT fk_category_media FOREIGN KEY (image_id) REFERENCES media(id) ON DELETE SET NULL,
ADD CONSTRAINT fk_parent_category FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL;