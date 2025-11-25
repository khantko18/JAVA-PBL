#!/bin/bash

# Database Setup Script for Cafe POS System
# Database: kkkDB
# User: root

echo "=========================================="
echo "  Cafe POS Database Setup"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Database credentials
DB_NAME="kkkDB"
DB_USER="root"
DB_PASSWORD="Khantkoko18$"

echo -e "${YELLOW}Step 1: Testing MySQL connection...${NC}"
mysql -u $DB_USER -p"$DB_PASSWORD" -e "SELECT 'Connection successful!' as Status;" 2>/dev/null

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ MySQL connection successful!${NC}"
else
    echo -e "${RED}❌ MySQL connection failed!${NC}"
    echo ""
    echo "Please check:"
    echo "1. MySQL is running: brew services list | grep mysql"
    echo "2. Password is correct: Khantkoko181$"
    echo "3. Root user has access"
    echo ""
    echo "Try connecting manually:"
    echo "mysql -u root -p"
    echo ""
    exit 1
fi

echo ""
echo -e "${YELLOW}Step 2: Creating database ${DB_NAME}...${NC}"
mysql -u $DB_USER -p"$DB_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS ${DB_NAME};" 2>/dev/null

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Database created successfully!${NC}"
else
    echo -e "${RED}❌ Failed to create database${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}Step 3: Loading database schema...${NC}"
mysql -u $DB_USER -p"$DB_PASSWORD" $DB_NAME < database_schema.sql 2>/dev/null

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Schema loaded successfully!${NC}"
else
    echo -e "${RED}❌ Failed to load schema${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}Step 4: Verifying tables...${NC}"
TABLES=$(mysql -u $DB_USER -p"$DB_PASSWORD" $DB_NAME -e "SHOW TABLES;" 2>/dev/null | tail -n +2)

if [ -z "$TABLES" ]; then
    echo -e "${RED}❌ No tables found${NC}"
    exit 1
else
    echo -e "${GREEN}✅ Tables created:${NC}"
    echo "$TABLES" | while read table; do
        echo "   - $table"
    done
fi

echo ""
echo -e "${YELLOW}Step 5: Checking sample data...${NC}"
COUNT=$(mysql -u $DB_USER -p"$DB_PASSWORD" $DB_NAME -e "SELECT COUNT(*) FROM menu_items;" 2>/dev/null | tail -n 1)

if [ "$COUNT" -gt 0 ]; then
    echo -e "${GREEN}✅ Sample data loaded: $COUNT menu items${NC}"
else
    echo -e "${YELLOW}⚠️  No sample data found${NC}"
fi

echo ""
echo "=========================================="
echo -e "${GREEN}  Database Setup Complete! ✅${NC}"
echo "=========================================="
echo ""
echo "Database Details:"
echo "  - Name: $DB_NAME"
echo "  - Host: localhost:3306"
echo "  - User: $DB_USER"
echo "  - Tables: menu_items, orders, order_items, payments"
echo ""
echo "Next Steps:"
echo "1. Compile: ./compile.sh"
echo "2. Run: ./run.sh"
echo ""
echo "The application will now connect to your MySQL database!"
echo ""

