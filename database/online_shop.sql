CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL UNIQUE,
    slug VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS brands (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE,
    logo VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS category_brands (
    brand_id INT,
    category_id INT,
    PRIMARY KEY (brand_id, category_id),
    FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE RESTRICT,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    price DECIMAL(15, 2) NOT NULL,
    discount_percent DECIMAL(3, 1) DEFAULT 0,
    in_stock BOOLEAN DEFAULT FALSE,
    published BOOLEAN DEFAULT FALSE,
    description VARCHAR(4096),
    main_image VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    category_id INT,
    brand_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT,
    FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE RESTRICT
);

CREATE TABLE product_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    product_id BIGINT,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL,
    password VARCHAR(255),
    avatar VARCHAR(255),
    role VARCHAR(32) NOT NULL,
    auth_provider VARCHAR(32) NOT NULL
);

INSERT INTO categories (name, slug)
VALUES 
	("Laptop", "laptop"),
    ("Điện thoại", "dien-thoai"),
    ("PC - Máy tính chơi game", "pc"),
    ("PC Workstation", "pc-workstation"),
    ("PC Văn phòng", "pc-van-phong"),
    ("Bàn phím", "ban-phim"),
    ("Chuột", "chuot"),
    ("Màn hình", "man-hinh")
;

INSERT INTO brands (name, logo)
VALUES 
	("Dell", "dell.png"),
    ("HP", "hp.png"),
    ("IPhone", "iphone.jpg")
;

INSERT INTO category_brands (brand_id, category_id)
VALUES 
	(1, 1),
    (2,1),
    (3,2)
;
