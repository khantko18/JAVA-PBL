-- Fix members table with correct amounts
USE kkkDB;

-- Delete old sample data
DELETE FROM members WHERE phone_number IN ('010-1234-5678', '010-2345-6789', '010-3456-7890', '010-4567-8901', '010-5678-9012');

-- Insert correct sample members
-- Base amounts that will display correctly in Korean (multiply by 1200 for Won)
INSERT INTO members (phone_number, name, total_spent, membership_level, discount_percent) 
VALUES 
    ('010-1234-5678', '김민수', 0, 5, 0.0),           -- ₩0 - Level 5
    ('010-2345-6789', '이지은', 625, 4, 5.0),         -- ₩750,000 - Level 4
    ('010-3456-7890', '박철수', 1250, 3, 10.0),       -- ₩1,500,000 - Level 3
    ('010-4567-8901', '최영희', 2083.33, 2, 15.0),    -- ₩2,500,000 - Level 2
    ('010-5678-9012', '정대한', 2916.67, 1, 20.0);    -- ₩3,500,000 - Level 1

-- Show results
SELECT phone_number, name, total_spent, membership_level, discount_percent FROM members;

