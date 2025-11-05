package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Model class for managing all menu items
 */
public class MenuManager {
    private Map<String, MenuItem> menuItems;
    private int nextId;
    
    public MenuManager() {
        this.menuItems = new HashMap<>();
        this.nextId = 1;
        initializeSampleMenu();
    }
    
    private void initializeSampleMenu() {
        // Add some sample menu items
        addMenuItem(new MenuItem("M001", "Americano", "Coffee", 3.50, "Classic espresso with hot water"));
        addMenuItem(new MenuItem("M002", "Cappuccino", "Coffee", 4.00, "Espresso with steamed milk foam"));
        addMenuItem(new MenuItem("M003", "Latte", "Coffee", 4.50, "Espresso with steamed milk"));
        addMenuItem(new MenuItem("M004", "Green Tea", "Beverage", 3.00, "Fresh brewed green tea"));
        addMenuItem(new MenuItem("M005", "Chocolate Cake", "Dessert", 5.50, "Rich chocolate cake slice"));
        addMenuItem(new MenuItem("M006", "Croissant", "Dessert", 3.50, "Butter croissant"));
    }
    
    public void addMenuItem(MenuItem item) {
        menuItems.put(item.getId(), item);
    }
    
    public void removeMenuItem(String id) {
        menuItems.remove(id);
    }
    
    public MenuItem getMenuItem(String id) {
        return menuItems.get(id);
    }
    
    public List<MenuItem> getAllMenuItems() {
        return new ArrayList<>(menuItems.values());
    }
    
    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItems.values().stream()
                       .filter(item -> item.getCategory().equals(category))
                       .collect(Collectors.toList());
    }
    
    public List<String> getAllCategories() {
        return menuItems.values().stream()
                       .map(MenuItem::getCategory)
                       .distinct()
                       .sorted()
                       .collect(Collectors.toList());
    }
    
    public String generateNewId() {
        return String.format("M%03d", nextId++);
    }
    
    public boolean menuItemExists(String id) {
        return menuItems.containsKey(id);
    }
}

