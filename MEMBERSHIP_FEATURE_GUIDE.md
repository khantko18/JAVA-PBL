# 👥 Membership Management System - Complete Guide

## Overview

A comprehensive membership loyalty program integrated into the Cafe POS System with automatic tier-based discounts, payment integration, and complete CRUD operations.

## Features

### ✨ Key Features

1. **Auto-Calculated Membership Levels** - Levels automatically update based on spending
2. **Tier-Based Discounts** - Automatic discount application at payment
3. **Full CRUD Operations** - Add, view, edit, delete members
4. **Phone Number Search** - Quick member lookup during payment
5. **Payment Integration** - Seamless discount application and spending updates
6. **Real-time Level Updates** - Membership tier recalculates after each purchase

## Membership Levels

| Level | Name     | Total Spent Requirement | Discount |
|-------|----------|------------------------|----------|
| 5     | Basic    | $0+                    | 0%       |
| 4     | Silver   | $500,000+              | 5%       |
| 3     | Gold     | $1,000,000+            | 10%      |
| 2     | Platinum | $2,000,000+            | 15%      |
| 1     | VIP      | $3,000,000+            | 20%      |

**Levels automatically update when totalSpent changes!**

## Quick Start

### Step 1: Setup Database

Run the SQL script to create the members table:

```bash
mysql -u root -p kkkDB < create_members_table.sql
```

Or manually:

```sql
USE kkkDB;
SOURCE create_members_table.sql;
```

### Step 2: Run Application

```bash
bash run.sh
```

### Step 3: Navigate to Membership Tab

Click on the **"👥 Membership"** tab (3rd tab, between Menu Management and Sales)

### Step 4: Add Your First Member

1. Enter phone number (e.g., `010-1234-5678`)
2. Enter name (e.g., `John Doe`)
3. Leave total spent at `0.0`
4. Click **"Add Member"**

### Step 5: Use at Payment

1. Go to Order tab and create an order
2. Click "Proceed to Payment"
3. Enter member's phone number
4. Click "Check Member"
5. Discount applies automatically!
6. Complete payment
7. Member's spending updates automatically

## Database Schema

```sql
CREATE TABLE members (
    phone_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    total_spent DECIMAL(12, 2) DEFAULT 0.0,
    membership_level INT NOT NULL DEFAULT 5,
    discount_percent DECIMAL(5, 2) NOT NULL DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_level (membership_level),
    INDEX idx_total_spent (total_spent)
);
```

**Primary Key:** `phone_number` (unique identifier)

## User Interface

### Membership Management View

The Membership tab provides a complete management interface:

#### Top Section - Member List

- **Search Bar** - Search by name or phone number
- **Refresh Button** - Reload member list from database
- **Total Members Counter** - Shows total registered members
- **Member Table** - Displays all members with:
  - Phone Number
  - Name
  - Total Spent
  - Membership Level
  - Current Discount
  - Amount to Next Level

#### Bottom Section - Add/Edit Form

- **Phone Number Field** - Enter or edit phone (required, unique)
- **Name Field** - Enter or edit member name (required)
- **Total Spent Field** - View or manually adjust spending
- **Action Buttons:**
  - **Add Member** - Register new member
  - **Update** - Save changes to selected member
  - **Delete** - Remove member from system
  - **Clear Form** - Reset all fields

### Payment Dialog Integration

Enhanced payment dialog with membership features:

1. **Membership Section** (Top)
   - Phone number input field
   - "Check Member" button
   - Member info display (name, level, discount)

2. **Payment Details** (Middle)
   - Original Amount (before discount)
   - Membership Discount (amount + percentage)
   - Final Amount (after discount) ✅
   - Payment method selection
   - Amount received & change calculation

3. **How It Works:**
   ```
   Original Amount: $100.00
   Member: John Doe - Level 3 (10% discount)
   Discount: -$10.00 (10%)
   Final Amount: $90.00 ✅
   ```

## Core Classes

### 1. Member.java (Model)

The Member class represents a loyalty program member.

```java
public class Member {
    private String phoneNumber;
    private String name;
    private double totalSpent;
    private int membershipLevel;
    private double discountPercent;
    
    // Auto-calculates level based on spending
    public void updateMembershipLevel();
    
    // Add to spending and recalculate level
    public void addSpending(double amount);
    
    // Calculate discount for given amount
    public double calculateDiscount(double originalAmount);
    
    // Get final amount after discount
    public double calculateFinalAmount(double originalAmount);
}
```

**Key Features:**
- Membership level auto-calculated from totalSpent
- Discount percentage determined by level
- Methods to calculate discounts and final amounts

### 2. MemberDAO.java (Database Access)

Handles all database operations for members.

```java
public class MemberDAO {
    // CRUD Operations
    public boolean insertMember(Member member);
    public Member getMemberByPhone(String phoneNumber);
    public List<Member> getAllMembers();
    public boolean updateMember(Member member);
    public boolean deleteMember(String phoneNumber);
    
    // Additional Operations
    public List<Member> searchMembers(String searchTerm);
    public List<Member> getMembersByLevel(int level);
    public int getTotalMemberCount();
}
```

### 3. MembershipController.java (Business Logic)

Controls membership operations and payment integration.

```java
public class MembershipController {
    // Member Management
    public boolean addMember(String phoneNumber, String name);
    public Member getMemberByPhone(String phoneNumber);
    public List<Member> getAllMembers();
    public List<Member> searchMembers(String searchTerm);
    public boolean updateMember(String oldPhone, String newPhone, String name, double totalSpent);
    public boolean deleteMember(String phoneNumber);
    
    // Payment Integration
    public MemberDiscountInfo getDiscountInfo(String phoneNumber, double originalAmount);
    public boolean applyPaymentToMember(String phoneNumber, double finalAmount);
}
```

### 4. MembershipView.java (UI Component)

Provides the user interface for membership management.

**Features:**
- Member table with sorting
- Search functionality
- Add/Edit/Delete operations
- Real-time membership level display
- Total member count

### 5. Enhanced PaymentDialog.java

Integrated payment dialog with membership support.

**New Features:**
- Phone number entry
- Member lookup
- Discount calculation display
- Final amount after discount
- Automatic spending update after payment

## Usage Examples

### Example 1: Adding a New Member

```java
MembershipController controller = new MembershipController();

// Add new member (starts at Level 5, 0% discount)
boolean success = controller.addMember("010-1234-5678", "John Doe");

if (success) {
    System.out.println("Member added successfully!");
}
```

### Example 2: Processing Payment with Membership

```java
// In PaymentDialog
String phoneNumber = "010-1234-5678";
double originalAmount = 100.00;

// Get member and calculate discount
Member member = membershipController.getMemberByPhone(phoneNumber);

if (member != null) {
    double discount = member.calculateDiscount(originalAmount);  // $10 if 10%
    double finalAmount = originalAmount - discount;  // $90
    
    // After successful payment
    member.addSpending(finalAmount);  // Add $90 to total spent
    memberDAO.updateMember(member);   // Save to database
}
```

### Example 3: Searching for Members

```java
// Search by name or phone
List<Member> results = controller.searchMembers("John");

// Get members by level
List<Member> vipMembers = controller.getMembersByLevel(1);

// Get all members
List<Member> allMembers = controller.getAllMembers();
```

### Example 4: Level Progression

```java
Member member = new Member("010-1234-5678", "Jane Smith", 0);

System.out.println(member.getMembershipLevel());  // 5 (Basic)
System.out.println(member.getDiscountPercent());  // 0.0%

// Customer makes purchases
member.addSpending(600000);

System.out.println(member.getMembershipLevel());  // 4 (Silver)
System.out.println(member.getDiscountPercent());  // 5.0%

// More purchases
member.addSpending(500000);  // Total: 1,100,000

System.out.println(member.getMembershipLevel());  // 3 (Gold)
System.out.println(member.getDiscountPercent());  // 10.0%
```

## Payment Flow with Membership

```
1. Cashier creates order → Total: $100.00

2. Click "Proceed to Payment"
   ↓
3. Enter customer phone number
   ↓
4. Click "Check Member"
   ↓
5. System finds member (Level 3 - Gold, 10% discount)
   ↓
6. Display:
   - Original Amount: $100.00
   - Discount: -$10.00 (10%)
   - Final Amount: $90.00 ✅
   ↓
7. Customer pays $90.00
   ↓
8. System updates member:
   - Old total spent: $1,000,000
   - Add $90.00
   - New total spent: $1,000,090
   - Level remains: 3 (Gold)
   ↓
9. Receipt shows:
   - Member: Jane Smith (Level 3 - Gold)
   - Original: $100.00
   - Discount: -$10.00
   - Final: $90.00
   - Payment successful!
```

## File Structure

```
PBL Project Ver3/
├── src/
│   ├── model/
│   │   └── Member.java                    ✨ NEW
│   ├── database/
│   │   └── MemberDAO.java                 ✨ NEW
│   ├── controller/
│   │   ├── MembershipController.java      ✨ NEW
│   │   └── OrderController.java           🔧 UPDATED
│   ├── view/
│   │   ├── MembershipView.java            ✨ NEW
│   │   ├── MainView.java                  🔧 UPDATED
│   │   └── PaymentDialog.java             🔧 UPDATED
│   └── util/
│       └── LanguageResources.java         🔧 UPDATED
│
├── create_members_table.sql               ✨ NEW
├── database_schema.sql                    🔧 UPDATED
└── MEMBERSHIP_FEATURE_GUIDE.md            ✨ NEW
```

## Testing

### Manual Testing Checklist

- [ ] Create database table
- [ ] Add new member
- [ ] Search for member by phone
- [ ] Search for member by name
- [ ] Edit member information
- [ ] Update member's total spent
- [ ] Delete member
- [ ] Make payment without membership
- [ ] Make payment with Level 5 member (0% discount)
- [ ] Make payment with Level 4 member (5% discount)
- [ ] Make payment with Level 3 member (10% discount)
- [ ] Verify member's total spent increases
- [ ] Verify level automatically upgrades
- [ ] View membership tab with multiple members
- [ ] Sort members by total spent

### Test Scenarios

#### Scenario 1: New Member Registration
1. Go to Membership tab
2. Enter phone: `010-9999-8888`
3. Enter name: `Test User`
4. Click "Add Member"
5. ✅ Verify member appears in table as Level 5

#### Scenario 2: Level Progression
1. Add member with $0 spent (Level 5)
2. Go to Order tab, create $550,000 order
3. At payment, enter member phone
4. Complete payment
5. Return to Membership tab
6. ✅ Verify member is now Level 4 with 5% discount

#### Scenario 3: Discount Application
1. Create order worth $100.00
2. At payment, enter VIP member phone (Level 1)
3. ✅ Verify original: $100.00
4. ✅ Verify discount: -$20.00 (20%)
5. ✅ Verify final: $80.00

## Troubleshooting

### Issue: "Phone number already exists"
**Solution:** Each phone number must be unique. Use a different number or update the existing member.

### Issue: Member not found during payment
**Solution:** 
- Check phone number format matches exactly
- Verify member exists in Membership tab
- Try searching in Membership tab first

### Issue: Level not updating after payment
**Solution:**
- Verify payment was successful
- Check member's total spent in Membership tab
- Refresh the member list

### Issue: Database connection error
**Solution:**
- Ensure MySQL is running
- Verify database `kkkDB` exists
- Check DatabaseManager connection settings

## Advanced Features

### Manually Adjust Member Spending

Useful for:
- Correcting errors
- Importing legacy data
- Promotional adjustments

**Steps:**
1. Select member in table
2. Edit "Total Spent" field
3. Click "Update"
4. Level recalculates automatically

### Bulk Import Members

Create SQL script:

```sql
INSERT INTO members (phone_number, name, total_spent, membership_level, discount_percent)
VALUES 
    ('010-1111-1111', 'Member 1', 0, 5, 0.0),
    ('010-2222-2222', 'Member 2', 750000, 4, 5.0),
    ('010-3333-3333', 'Member 3', 1500000, 3, 10.0);
```

### Export Member Data

```sql
SELECT phone_number, name, total_spent, membership_level, discount_percent
FROM members
INTO OUTFILE '/tmp/members.csv'
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n';
```

## Best Practices

1. **Phone Number Format** - Use consistent format (e.g., `010-XXXX-XXXX`)
2. **Verify Before Delete** - Always confirm before deleting members
3. **Regular Backups** - Backup member data regularly
4. **Privacy** - Handle member data securely
5. **Level Transparency** - Clearly communicate level benefits to customers

## Future Enhancements

Potential features for future versions:

- [ ] Member points system
- [ ] Birthday specials
- [ ] Email/SMS notifications
- [ ] Member cards with barcodes
- [ ] Purchase history per member
- [ ] Expiry dates for levels
- [ ] Referral program
- [ ] Member analytics dashboard
- [ ] Bulk SMS campaigns
- [ ] Member mobile app

## API Reference

### Member Class Methods

```java
// Constructors
Member(String phoneNumber, String name, double totalSpent)

// Level Management
void updateMembershipLevel()
void addSpending(double amount)

// Calculations
double calculateDiscount(double originalAmount)
double calculateFinalAmount(double originalAmount)

// Utilities
String getLevelName()
String getLevelDescription()
double getAmountToNextLevel()

// Getters & Setters
String getPhoneNumber()
String getName()
double getTotalSpent()
int getMembershipLevel()
double getDiscountPercent()
void setTotalSpent(double totalSpent)  // Auto-updates level
```

### MembershipController Methods

```java
// CRUD
boolean addMember(String phoneNumber, String name)
Member getMemberByPhone(String phoneNumber)
List<Member> getAllMembers()
boolean updateMember(String oldPhone, String newPhone, String name, double totalSpent)
boolean deleteMember(String phoneNumber)

// Search & Filter
List<Member> searchMembers(String searchTerm)
List<Member> getMembersByLevel(int level)
int getTotalMemberCount()

// Payment Integration
MemberDiscountInfo getDiscountInfo(String phoneNumber, double originalAmount)
boolean applyPaymentToMember(String phoneNumber, double finalAmount)
```

## Support

For issues or questions:
1. Check this guide first
2. Review console output for errors
3. Verify database connection
4. Check member data in database directly

---

**Version:** 1.0  
**Last Updated:** November 2025  
**Status:** ✅ Production Ready

