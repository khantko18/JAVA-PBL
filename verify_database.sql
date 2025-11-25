-- Verify and show database tables
USE pos_system;

-- Show all tables
SHOW TABLES;

-- Check if orders table exists
SELECT COUNT(*) as orders_table_exists 
FROM information_schema.tables 
WHERE table_schema = 'pos_system' 
AND table_name = 'orders';

-- Check if payments table exists
SELECT COUNT(*) as payments_table_exists 
FROM information_schema.tables 
WHERE table_schema = 'pos_system' 
AND table_name = 'payments';

-- Check if order_items table exists
SELECT COUNT(*) as order_items_table_exists 
FROM information_schema.tables 
WHERE table_schema = 'pos_system' 
AND table_name = 'order_items';

-- Check if members table exists
SELECT COUNT(*) as members_table_exists 
FROM information_schema.tables 
WHERE table_schema = 'pos_system' 
AND table_name = 'members';

