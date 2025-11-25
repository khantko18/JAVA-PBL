-- Fix total_spent precision for accurate Won conversion
USE kkkDB;

-- Change total_spent to have 4 decimal places (was 2)
ALTER TABLE members MODIFY COLUMN total_spent DECIMAL(12, 4) DEFAULT 0.0;

-- Show the updated structure
DESCRIBE members;

-- Update existing members to recalculate with new precision
-- This ensures clean numbers
SELECT phone_number, name, total_spent FROM members;

