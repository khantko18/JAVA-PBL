-- Quick Guide: Add Images to Menu Items
-- Run this after placing images in images/menu_items/ directory

USE kkkDB;

-- Example: Add images to menu items
-- Replace the paths with your actual image files

-- Coffee Items
UPDATE menu_items SET image_path = 'images/menu_items/americano.jpg' WHERE id = 'M001' AND name = 'Americano';
UPDATE menu_items SET image_path = 'images/menu_items/cappuccino.jpg' WHERE id = 'M002' AND name = 'Cappuccino';
UPDATE menu_items SET image_path = 'images/menu_items/latte.jpg' WHERE id = 'M003' AND name = 'Latte';

-- Beverage Items
UPDATE menu_items SET image_path = 'images/menu_items/green-tea.jpg' WHERE id = 'M004' AND name = 'Green Tea';

-- Dessert Items
UPDATE menu_items SET image_path = 'images/menu_items/chocolate-cake.jpg' WHERE id = 'M005' AND name = 'Chocolate Cake';
UPDATE menu_items SET image_path = 'images/menu_items/croissant.jpg' WHERE id = 'M006' AND name = 'Croissant';

-- Verify changes
SELECT id, name, category, 
       CASE 
           WHEN image_path IS NULL THEN '❌ No Image'
           ELSE CONCAT('✅ ', image_path)
       END as image_status
FROM menu_items
ORDER BY id;

-- Count items with/without images
SELECT 
    COUNT(CASE WHEN image_path IS NOT NULL THEN 1 END) as items_with_images,
    COUNT(CASE WHEN image_path IS NULL THEN 1 END) as items_without_images,
    COUNT(*) as total_items
FROM menu_items;

