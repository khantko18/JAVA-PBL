package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages language switching between English and Korean
 */
public class LanguageManager {
    private static LanguageManager instance;
    private Language currentLanguage;
    private List<LanguageChangeListener> listeners;
    
    public enum Language {
        ENGLISH, KOREAN
    }
    
    public interface LanguageChangeListener {
        void onLanguageChanged(Language newLanguage);
    }
    
    private LanguageManager() {
        currentLanguage = Language.ENGLISH;
        listeners = new ArrayList<>();
    }
    
    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }
    
    public Language getCurrentLanguage() {
        return currentLanguage;
    }
    
    public void setLanguage(Language language) {
        if (this.currentLanguage != language) {
            this.currentLanguage = language;
            notifyListeners();
        }
    }
    
    public void addLanguageChangeListener(LanguageChangeListener listener) {
        listeners.add(listener);
    }
    
    public void removeLanguageChangeListener(LanguageChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners() {
        for (LanguageChangeListener listener : listeners) {
            listener.onLanguageChanged(currentLanguage);
        }
    }
    
    public String getText(String key) {
        if (currentLanguage == Language.KOREAN) {
            return LanguageResources.getKorean(key);
        } else {
            return LanguageResources.getEnglish(key);
        }
    }
    
    public String getCurrencySymbol() {
        if (currentLanguage == Language.KOREAN) {
            return "₩";
        } else {
            return "$";
        }
    }
    
    public String formatPrice(double price) {
        if (currentLanguage == Language.KOREAN) {
            // For Korean, multiply by 1000 to convert to Won (approximate exchange rate)
            return "₩ " + String.format("%,.0f", price * 1200);
        } else {
            return "$ " + String.format("%.2f", price);
        }
    }
    
    public String translateCategory(String englishCategory) {
        if (currentLanguage == Language.KOREAN) {
            switch (englishCategory) {
                case "Coffee": return "커피";
                case "Beverage": return "음료";
                case "Dessert": return "디저트";
                case "Food": return "음식";
                default: return englishCategory;
            }
        }
        return englishCategory;
    }
    
    public String getCategoryKey(String displayCategory) {
        if (displayCategory == null) {
            return null;
        }
        
        if (currentLanguage == Language.KOREAN) {
            switch (displayCategory) {
                case "커피": return "Coffee";
                case "음료": return "Beverage";
                case "디저트": return "Dessert";
                case "음식": return "Food";
                default: return displayCategory;
            }
        }
        return displayCategory;
    }
}

