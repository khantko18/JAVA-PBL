#!/bin/bash
# Cafe POS System - Database Setup Script

cd "/Users/khantkoko1999/eclipse-workspace/PBL Project"

echo "Setting up MySQL database..."
echo ""
echo "Database: kkkdb"
echo "User: root"
echo ""

# Run the SQL schema
mysql -u root -p123abcABC% < database_schema.sql

if [ $? -eq 0 ]; then
    echo ""
    echo "Database setup complete!"
    echo ""
    echo "Verifying tables..."
    mysql -u root -p123abcABC% kkkdb -e "SHOW TABLES;"
    echo ""
    echo "Checking sample menu items..."
    mysql -u root -p123abcABC% kkkdb -e "SELECT COUNT(*) as 'Menu Items' FROM menu_items;"
else
    echo ""
    echo "Database setup failed!"
    echo ""
    echo "Please check:"
    echo "1. MySQL is running: brew services list"
    echo "2. Password is correct: 123abcABC%"
    echo "3. You have permissions to create databases"
    exit 1
fi

