-- liquibase formatted sql

-- changeset xdpsx:issue-11
-- comment: Update media table
CREATE TABLE IF NOT EXISTS media (
    id VARCHAR(30) NOT NULL PRIMARY KEY, -- display_name in cloudinary
    external_id VARCHAR(50) NOT NULL UNIQUE, -- public_id in cloudinary
    url VARCHAR(255) NOT NULL,
    caption VARCHAR(100),
    content_type VARCHAR(30) NOT NULL,
    resource_type TINYINT(1) NOT NULL,
    temp_flg BOOLEAN DEFAULT FALSE,
    delete_flg BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);