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

SELECT * FROM items;

CREATE TABLE invoices (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_number VARCHAR(50) UNIQUE NOT NULL,
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    billing_type VARCHAR(20),
    subtotal DECIMAL(10,2),
    discount DECIMAL(10,2),
    total DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'Active'
);

SELECT * FROM invoices;

CREATE TABLE invoice_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT,
    item_id INT,
    quantity INT,
    price DECIMAL(10,2),
    total DECIMAL(10,2),
    FOREIGN KEY (invoice_id) REFERENCES invoices(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);

SELECT * FROM invoice_items;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

SELECT * FROM users;

INSERT INTO users (username, password)
VALUES ('admin', '123456');