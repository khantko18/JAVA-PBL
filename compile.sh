#!/bin/bash
# Cafe POS System - Compile Script

cd "/Users/khantkoko1999/eclipse-workspace/PBL Project"

echo "🔨 Compiling Cafe POS System..."
echo ""

javac -source 11 -target 11 -cp ".:lib/mysql-connector-j-9.4.0.jar" -d bin \
  src/database/*.java \
  src/util/*.java \
  src/model/*.java \
  src/view/*.java \
  src/controller/*.java \
  src/POSApplication.java

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Compilation successful!"
    echo ""
    echo "Run with: ./run.sh"
    echo "Or: java -cp \"bin:lib/mysql-connector-j-9.4.0.jar\" POSApplication"
else
    echo ""
    echo "❌ Compilation failed!"
    exit 1
fi

