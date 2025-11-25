# 👥 Membership System - Quick Start

## Setup (2 Steps)

### 1. Create Database Table

```bash
mysql -u root -p kkkDB < create_members_table.sql
```

### 2. Run Application

```bash
bash run.sh
```

## How to Use

### Adding Members

1. Click **"👥 Membership"** tab
2. Enter phone number & name
3. Click **"Add Member"**
4. Done! Member starts at Level 5 (0% discount)

### Using at Payment

1. Create order in **Order** tab
2. Click **"Proceed to Payment"**
3. Enter customer phone number
4. Click **"Check Member"**
5. Discount applies automatically! ✅
6. Complete payment
7. Member's spending updates automatically

## Membership Levels

| Level | Requirement | Discount |
|-------|-------------|----------|
| 5     | $0+         | 0%       |
| 4     | $500K+      | 5%       |
| 3     | $1M+        | 10%      |
| 2     | $2M+        | 15%      |
| 1     | $3M+        | 20%      |

**Levels update automatically when customer spends money!**

## Example

```
Customer: John Doe (Level 3 - Gold)
Order Total: $100.00
Discount: -$10.00 (10%)
Final Amount: $90.00 ✅

After payment:
- John's total spent: $1,000,090
- Still Level 3 (needs $2M for Level 2)
```

## Features

✅ Auto-calculated levels  
✅ Automatic discounts  
✅ Phone number search  
✅ Add/Edit/Delete members  
✅ Real-time updates  
✅ Payment integration  

## Sample Members

5 test members are created automatically:
- `010-1234-5678` - Level 5 ($0)
- `010-2345-6789` - Level 4 ($750K)
- `010-3456-7890` - Level 3 ($1.5M)
- `010-4567-8901` - Level 2 ($2.5M)
- `010-5678-9012` - Level 1 ($3.5M)

## Need Help?

See full documentation: `MEMBERSHIP_FEATURE_GUIDE.md`

