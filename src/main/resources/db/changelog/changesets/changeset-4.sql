-- liquibase formatted sql

-- changeset xdpsx:issue-9
-- comment: Update brands table
ALTER TABLE brands
DROP COLUMN logo;

ALTER TABLE brands
MODIFY COLUMN created_at TIMESTAMP NOT NULL,
MODIFY COLUMN updated_at TIMESTAMP;

ALTER TABLE brands
ADD COLUMN public_flg BOOLEAN NOT NULL DEFAULT FALSE AFTER name,
ADD COLUMN image_id VARCHAR(30) AFTER public_flg,
ADD CONSTRAINT fk_brand_media FOREIGN KEY (image_id) REFERENCES media(id) ON DELETE SET NULL;