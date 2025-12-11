#!/bin/bash

echo "======================================"
echo "Setting up ALL database tables"
echo "======================================"

# Set your MySQL password here
MYSQL_PASSWORD="root"

echo "Creating database and all tables..."
mysql -u root -p"$MYSQL_PASSWORD" < database_schema.sql

if [ $? -eq 0 ]; then
    echo "✅ Database and tables created successfully!"
else
    echo "❌ Error creating database. Please check your MySQL password."
    echo "If you have a different password, edit this file and change MYSQL_PASSWORD"
    exit 1
fi

echo ""
echo "Loading sample members data..."
mysql -u root -p"$MYSQL_PASSWORD" < create_members_table.sql

if [ $? -eq 0 ]; then
    echo "✅ Sample members loaded successfully!"
else
    echo "⚠️  Error loading sample members (table may already exist)"
fi

echo ""
echo "Verifying database setup..."
mysql -u root -p"$MYSQL_PASSWORD" < verify_database.sql

echo ""
echo "======================================"
echo "Setup complete!"
echo "======================================"
echo ""
echo "You can now run the application:"
echo "  javac -d bin -cp \"lib/*:src\" src/*.java src/**/*.java"
echo "  java -cp \"bin:lib/*\" POSApplication"

