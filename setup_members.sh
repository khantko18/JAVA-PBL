#!/bin/bash
# Setup members table

echo "Setting up members table..."

# Prompt for MySQL password
read -sp "Enter MySQL root password (press Enter if no password): " MYSQL_PASS
echo ""

if [ -z "$MYSQL_PASS" ]; then
    mysql -u root kkkDB < create_members_table.sql
else
    mysql -u root -p"$MYSQL_PASS" kkkDB < create_members_table.sql
fi

if [ $? -eq 0 ]; then
    echo "✅ Members table created successfully!"
    echo "✅ Sample members added"
else
    echo "❌ Error creating table. Please check your database connection."
fi

