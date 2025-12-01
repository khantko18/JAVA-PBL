package model;

/**
 * Model class representing a member in the cafe loyalty program
 */
public class Member {
    private String phoneNumber; // Unique identifier
    private String name;
    private double totalSpent;
    private int membershipLevel;
    private double discountPercent;
    
    // Membership level thresholds (in base currency - converts to Won when displayed)
    // 50만원, 100만원, 200만원, 300만원
    public static final double LEVEL_4_THRESHOLD = 416.67;  // 500,000 Won
    public static final double LEVEL_3_THRESHOLD = 833.33;  // 1,000,000 Won
    public static final double LEVEL_2_THRESHOLD = 1666.67; // 2,000,000 Won
    public static final double LEVEL_1_THRESHOLD = 2500.0;  // 3,000,000 Won
    
    // Discount rates
    public static final double LEVEL_5_DISCOUNT = 0.0;  // 0%
    public static final double LEVEL_4_DISCOUNT = 5.0;  // 5%
    public static final double LEVEL_3_DISCOUNT = 10.0; // 10%
    public static final double LEVEL_2_DISCOUNT = 15.0; // 15%
    public static final double LEVEL_1_DISCOUNT = 20.0; // 20%
    
    public Member(String phoneNumber, String name, double totalSpent) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.totalSpent = totalSpent;
        updateMembershipLevel();
    }
    
    /**
     * Calculate and update membership level based on total spent
     */
    public void updateMembershipLevel() {
        if (totalSpent >= LEVEL_1_THRESHOLD) {
            membershipLevel = 1;
            discountPercent = LEVEL_1_DISCOUNT;
        } else if (totalSpent >= LEVEL_2_THRESHOLD) {
            membershipLevel = 2;
            discountPercent = LEVEL_2_DISCOUNT;
        } else if (totalSpent >= LEVEL_3_THRESHOLD) {
            membershipLevel = 3;
            discountPercent = LEVEL_3_DISCOUNT;
        } else if (totalSpent >= LEVEL_4_THRESHOLD) {
            membershipLevel = 4;
            discountPercent = LEVEL_4_DISCOUNT;
        } else {
            membershipLevel = 5;
            discountPercent = LEVEL_5_DISCOUNT;
        }
    }
    
    /**
     * Add amount to total spent and recalculate level
     */
    public void addSpending(double amount) {
        this.totalSpent += amount;
        updateMembershipLevel();
    }
    
    /**
     * Calculate discount amount based on original amount
     */
    public double calculateDiscount(double originalAmount) {
        return originalAmount * (discountPercent / 100.0);
    }
    
    /**
     * Calculate final amount after discount
     */
    public double calculateFinalAmount(double originalAmount) {
        return originalAmount - calculateDiscount(originalAmount);
    }
    
    /**
     * Get membership level name
     */
    public String getLevelName() {
        return "Level " + membershipLevel;
    }
    
    /**
     * Get level description
     */
    public String getLevelDescription() {
        switch (membershipLevel) {
            case 1:
                return "Level 1 - VIP (20% discount)";
            case 2:
                return "Level 2 - Platinum (15% discount)";
            case 3:
                return "Level 3 - Gold (10% discount)";
            case 4:
                return "Level 4 - Silver (5% discount)";
            case 5:
            default:
                return "Level 5 - Basic (0% discount)";
        }
    }
    
    /**
     * Get amount needed for next level
     */
    public double getAmountToNextLevel() {
        switch (membershipLevel) {
            case 5:
                return LEVEL_4_THRESHOLD - totalSpent;
            case 4:
                return LEVEL_3_THRESHOLD - totalSpent;
            case 3:
                return LEVEL_2_THRESHOLD - totalSpent;
            case 2:
                return LEVEL_1_THRESHOLD - totalSpent;
            case 1:
            default:
                return 0; // Already at highest level
        }
    }
    
    // Getters and Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getTotalSpent() {
        return totalSpent;
    }
    
    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
        updateMembershipLevel();
    }
    
    public int getMembershipLevel() {
        return membershipLevel;
    }
    
    public double getDiscountPercent() {
        return discountPercent;
    }
    
    @Override
    public String toString() {
        return name + " (" + phoneNumber + ") - " + getLevelName() + " - $" + String.format("%.2f", totalSpent);
    }
}

