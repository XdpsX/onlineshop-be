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
