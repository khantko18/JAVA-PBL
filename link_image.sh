#!/bin/bash
# Helper script to link an image file to a menu item
# Usage: ./link_image.sh <item_id> <image_filename>
# Example: ./link_image.sh M001 IcedAmericano.jpg

if [ $# -lt 2 ]; then
    echo "Usage: ./link_image.sh <item_id> <image_filename>"
    echo "Example: ./link_image.sh M001 IcedAmericano.jpg"
    exit 1
fi

ITEM_ID=$1
IMAGE_FILE=$2
IMAGE_PATH="images/menu_items/$IMAGE_FILE"

# Check if image file exists
if [ ! -f "images/menu_items/$IMAGE_FILE" ]; then
    echo "❌ Error: Image file not found: images/menu_items/$IMAGE_FILE"
    echo "Please make sure the image file exists in the images/menu_items/ directory"
    exit 1
fi

# Update database
mysql -h 127.0.0.1 -u root -p'Khantkoko18$' kkkDB << EOF 2>&1 | grep -v "Warning"
UPDATE menu_items 
SET image_path = '$IMAGE_PATH'
WHERE id = '$ITEM_ID';

SELECT id, name, category, image_path 
FROM menu_items 
WHERE id = '$ITEM_ID';
EOF

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Successfully linked image to menu item!"
    echo "   Item ID: $ITEM_ID"
    echo "   Image: $IMAGE_PATH"
else
    echo "❌ Failed to update database"
    exit 1
fi

