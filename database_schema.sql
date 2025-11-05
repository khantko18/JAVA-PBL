-- Cafe POS System Database Schema
-- Database: khantkoko

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS kkkdb;
USE kkkdb;

-- 1. Menu Items Table
CREATE TABLE IF NOT EXISTS menu_items (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Orders Table
CREATE TABLE IF NOT EXISTS orders (
    order_id VARCHAR(20) PRIMARY KEY,
    order_date DATE NOT NULL,
    order_time TIME NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    discount_percent DECIMAL(5, 2) DEFAULT 0,
    discount_amount DECIMAL(10, 2) DEFAULT 0,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Order Items Table (Order Details)
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(20) NOT NULL,
    menu_item_id VARCHAR(10) NOT NULL,
    menu_item_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- 4. Payments Table
CREATE TABLE IF NOT EXISTS payments (
    payment_id VARCHAR(20) PRIMARY KEY,
    order_id VARCHAR(20) NOT NULL,
    payment_date DATE NOT NULL,
    payment_time TIME NOT NULL,
    payment_method VARCHAR(20) NOT NULL, -- 'CASH' or 'CARD'
    amount DECIMAL(10, 2) NOT NULL,
    received_amount DECIMAL(10, 2),
    change_amount DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- 5. Sales Summary View (for easy reporting)
CREATE OR REPLACE VIEW sales_summary AS
SELECT 
    DATE(p.payment_date) as sale_date,
    COUNT(DISTINCT p.order_id) as total_orders,
    SUM(p.amount) as total_revenue,
    AVG(p.amount) as average_order_value,
    SUM(CASE WHEN p.payment_method = 'CASH' THEN 1 ELSE 0 END) as cash_payments,
    SUM(CASE WHEN p.payment_method = 'CARD' THEN 1 ELSE 0 END) as card_payments
FROM payments p
GROUP BY DATE(p.payment_date)
ORDER BY sale_date DESC;

-- 6. Popular Items View
CREATE OR REPLACE VIEW popular_items AS
SELECT 
    oi.menu_item_id,
    oi.menu_item_name,
    mi.category,
    SUM(oi.quantity) as total_quantity_sold,
    SUM(oi.subtotal) as total_revenue,
    COUNT(DISTINCT oi.order_id) as order_count
FROM order_items oi
LEFT JOIN menu_items mi ON oi.menu_item_id = mi.id
GROUP BY oi.menu_item_id, oi.menu_item_name, mi.category
ORDER BY total_quantity_sold DESC;

-- Insert sample menu items
INSERT INTO menu_items (id, name, category, price, description) VALUES
('M001', 'Americano', 'Coffee', 3.50, 'Classic espresso with hot water'),
('M002', 'Cappuccino', 'Coffee', 4.00, 'Espresso with steamed milk foam'),
('M003', 'Latte', 'Coffee', 4.50, 'Espresso with steamed milk'),
('M004', 'Green Tea', 'Beverage', 3.00, 'Fresh brewed green tea'),
('M005', 'Chocolate Cake', 'Dessert', 5.50, 'Rich chocolate cake slice'),
('M006', 'Croissant', 'Dessert', 3.50, 'Butter croissant')
ON DUPLICATE KEY UPDATE name=name;

-- Create indexes for better performance
CREATE INDEX idx_orders_date ON orders(order_date);
CREATE INDEX idx_payments_date ON payments(payment_date);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_menu_category ON menu_items(category);

-- Show tables
SHOW TABLES;

