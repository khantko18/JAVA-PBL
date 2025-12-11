-- Create members table for membership program
-- Database: kkkDB

USE kkkDB;

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

-- Insert sample members for testing
INSERT INTO members (phone_number, name, total_spent, membership_level, discount_percent) 
VALUES 
    ('010-1234-5678', '김민수', 0, 5, 0.0),
    ('010-2345-6789', '이지은', 625, 4, 5.0),       -- 750,000 Won
    ('010-3456-7890', '박철수', 1250, 3, 10.0),     -- 1,500,000 Won
    ('010-4567-8901', '최영희', 2083.33, 2, 15.0),  -- 2,500,000 Won
    ('010-5678-9012', '정대한', 2916.67, 1, 20.0)   -- 3,500,000 Won
ON DUPLICATE KEY UPDATE name = name;

-- Show results
SELECT * FROM members ORDER BY total_spent DESC;

