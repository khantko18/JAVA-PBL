package model;

/**
 * Model class representing a menu item in the cafe
 * 이미지 경로(imagePath) 필드 추가 (기존 생성자, 메서드는 유지하여 하위 호환성 보장)
 */
public class MenuItem {
    private String id;
    private String name;
    private String category; // Coffee, Beverage, Dessert, etc.
    private double price;
    private String description;
    private boolean available;
    private String imagePath; // 새로 추가된 필드

    // 기존 생성자 (하위 호환성)
    public MenuItem(String id, String name, String category, double price, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.available = true;
        this.imagePath = null;
    }

    // 새 생성자(이미지 경로 포함)
    public MenuItem(String id, String name, String category, double price, String description, String imagePath) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.available = true;
        this.imagePath = imagePath;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public boolean isAvailable() { return available; }
    public String getImagePath() { return imagePath; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        return name + " - $" + String.format("%.2f", price);
    }
}
