-- Cafe POS System Database Schema
-- Database: khantkoko

-- 1. 데이터베이스 생성 및 선택
CREATE DATABASE IF NOT EXISTS kkkdb;
USE kkkdb;

-- [초기화] 기존 테이블 삭제 (순서 중요)
DROP VIEW IF EXISTS sales_summary;
DROP VIEW IF EXISTS popular_items;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS menu_items;
DROP TABLE IF EXISTS members;

-- 2. Menu Items Table
CREATE TABLE IF NOT EXISTS menu_items (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    available BOOLEAN DEFAULT TRUE,
    image_path VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. Members Table (수정됨: membership_level, discount_percent 추가)
CREATE TABLE IF NOT EXISTS members (
    member_id VARCHAR(20) PRIMARY KEY, -- 내부 관리용 ID (MEM001 등) - 실제 조회는 phone_number로 함
    name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    points INT DEFAULT 0,
    total_spent DECIMAL(10, 2) DEFAULT 0.00,
    membership_level INT DEFAULT 5, -- [추가] 레벨 (1~5)
    discount_percent DECIMAL(5, 2) DEFAULT 0.00, -- [추가] 할인율
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 4. Orders Table
CREATE TABLE IF NOT EXISTS orders (
    order_id VARCHAR(20) PRIMARY KEY,
    member_id VARCHAR(20), -- phone_number를 저장하거나 member_id를 저장 (여기서는 phone_number를 외래키로 안 쓰고 단순 기록용으로 가정하거나, members의 PK를 참조)
    -- 외래키 제약조건은 members 테이블의 PK가 member_id이므로 member_id를 저장해야 함.
    -- 하지만 로직상 phone_number로 회원을 찾으므로, 편의상 외래키를 잠시 제거하거나 member_id를 저장하도록 로직 통일 필요.
    -- 여기서는 외래키 제약조건을 잠시 제거하여 유연성을 확보합니다. (실무에서는 엄격하게 관리)
    order_date DATE NOT NULL,
    order_time TIME NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    discount_percent DECIMAL(5, 2) DEFAULT 0,
    discount_amount DECIMAL(10, 2) DEFAULT 0,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Order Items Table
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

-- 6. Payments Table
CREATE TABLE IF NOT EXISTS payments (
    payment_id VARCHAR(20) PRIMARY KEY,
    order_id VARCHAR(20) NOT NULL,
    payment_date DATE NOT NULL,
    payment_time TIME NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    received_amount DECIMAL(10, 2),
    change_amount DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

<<<<<<< HEAD
-- 7. Views
=======
-- 5. Members Table (Membership Program)
CREATE TABLE IF NOT EXISTS members (
    phone_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    total_spent DECIMAL(12, 4) DEFAULT 0.0,
    membership_level INT NOT NULL DEFAULT 5,
    discount_percent DECIMAL(5, 2) NOT NULL DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_level (membership_level),
    INDEX idx_total_spent (total_spent)
);

-- 6. Sales Summary View (for easy reporting)
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
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

<<<<<<< HEAD
=======
-- 7. Popular Items View
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
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

-- 8. Insert Sample Data
INSERT INTO menu_items (id, name, category, price, description, image_path) VALUES
('M001', 'Americano', 'Coffee', 3.50, 'Classic espresso with hot water', NULL),
('M002', 'Cappuccino', 'Coffee', 4.00, 'Espresso with steamed milk foam', NULL),
('M003', 'Latte', 'Coffee', 4.50, 'Espresso with steamed milk', NULL),
('M004', 'Green Tea', 'Beverage', 3.00, 'Fresh brewed green tea', NULL),
('M005', 'Chocolate Cake', 'Dessert', 5.50, 'Rich chocolate cake slice', NULL),
('M006', 'Croissant', 'Dessert', 3.50, 'Butter croissant', NULL)
ON DUPLICATE KEY UPDATE name=VALUES(name);

<<<<<<< HEAD
-- [수정] 샘플 멤버 데이터 (누락된 컬럼 포함)
INSERT INTO members (member_id, name, phone_number, points, total_spent, membership_level, discount_percent) VALUES
('MEM001', 'Kim Cheolsu', '010-1234-5678', 100, 500.00, 4, 5.00),
('MEM002', 'Lee Yeonghee', '010-9876-5432', 250, 1200.50, 3, 10.00),
('MEM003', 'Park Minsoo', '010-5555-7777', 0, 0.00, 5, 0.00)
ON DUPLICATE KEY UPDATE points=VALUES(points);

-- Indexes
=======
-- Create indexes for better performance (drop first if they exist)
DROP INDEX IF EXISTS idx_orders_date ON orders;
>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
CREATE INDEX idx_orders_date ON orders(order_date);

DROP INDEX IF EXISTS idx_payments_date ON payments;
CREATE INDEX idx_payments_date ON payments(payment_date);

DROP INDEX IF EXISTS idx_order_items_order ON order_items;
CREATE INDEX idx_order_items_order ON order_items(order_id);

DROP INDEX IF EXISTS idx_menu_category ON menu_items;
CREATE INDEX idx_menu_category ON menu_items(category);
CREATE INDEX idx_members_phone ON members(phone_number);

SHOW TABLES;