CREATE DATABASE pos_db;
USE pos_db;

CREATE TABLE items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_code VARCHAR(50) UNIQUE NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    cost DECIMAL(10,2),
    wholesale_price DECIMAL(10,2),
    retail_price DECIMAL(10,2),
    label_price DECIMAL(10,2),
    credit_price DECIMAL(10,2),
    status VARCHAR(10) DEFAULT 'Active',
    image_path VARCHAR(255)
);