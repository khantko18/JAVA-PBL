-- Add image column to menu_items table
-- Run this to add image support to existing database

USE kkkDB;

-- Add image_path column to store the path to menu item images
-- If column already exists, this will show an error but won't break the database
ALTER TABLE menu_items 
ADD COLUMN image_path VARCHAR(255) DEFAULT NULL
AFTER description;

-- Verify the change
DESCRIBE menu_items;

-- Show current items
SELECT id, name, category, image_path FROM menu_items;

