package model;

/**
 * Model class representing a menu item in the cafe
 */
public class MenuItem {
    private String id;
    private String name;
    private String category; // Coffee, Beverage, Dessert, etc.
    private double price;
    private String description;
    private boolean available;
    
    public MenuItem(String id, String name, String category, double price, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.available = true;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public boolean isAvailable() { return available; }
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setAvailable(boolean available) { this.available = available; }
    
    @Override
    public String toString() {
        return name + " - $" + String.format("%.2f", price);
    }
}

