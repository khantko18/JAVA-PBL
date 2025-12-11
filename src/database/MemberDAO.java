package database;

import model.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Member
 */
public class MemberDAO {
    
    public MemberDAO() {
        // Get fresh connection each time
    }
    
    // Helper method to get fresh connection
    private Connection getConnection() {
        return DatabaseManager.getInstance().getConnection();
    }
    
    // Create - Add new member
    public boolean insertMember(Member member) {
        Connection connection = getConnection();
        if (connection == null) {
            System.err.println("⚠️ Database connection is null! Cannot insert member.");
            return false;
        }
        
        // Check if phone number already exists
        if (getMemberByPhone(member.getPhoneNumber()) != null) {
            System.err.println("⚠️ Member with phone " + member.getPhoneNumber() + " already exists.");
            return false;
        }
        
        String sql = "INSERT INTO members (phone_number, name, total_spent, membership_level, discount_percent) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, member.getPhoneNumber());
            pstmt.setString(2, member.getName());
            pstmt.setDouble(3, member.getTotalSpent());
            pstmt.setInt(4, member.getMembershipLevel());
            pstmt.setDouble(5, member.getDiscountPercent());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("⚠️ SQL Error inserting member: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Read - Get member by phone number (works with or without hyphens)
    public Member getMemberByPhone(String phoneNumber) {
        Connection connection = getConnection();
        if (connection == null) {
            return null;
        }
        
        // Try exact match first
        String sql = "SELECT * FROM members WHERE phone_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, phoneNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Member member = new Member(
                        rs.getString("phone_number"),
                        rs.getString("name"),
                        rs.getDouble("total_spent")
                    );
                    return member;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // If not found and search term has no hyphens, try matching without hyphens
        if (!phoneNumber.contains("-")) {
            sql = "SELECT * FROM members WHERE REPLACE(phone_number, '-', '') = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, phoneNumber);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Member member = new Member(
                            rs.getString("phone_number"),
                            rs.getString("name"),
                            rs.getDouble("total_spent")
                        );
                        return member;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    // Read - Get all members
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        Connection connection = getConnection();
        if (connection == null) {
            return members;
        }
        
        String sql = "SELECT * FROM members ORDER BY total_spent DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Member member = new Member(
                    rs.getString("phone_number"),
                    rs.getString("name"),
                    rs.getDouble("total_spent")
                );
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return members;
    }
    
    // Read - Search members by name or phone
    public List<Member> searchMembers(String searchTerm) {
        List<Member> members = new ArrayList<>();
        Connection connection = getConnection();
        if (connection == null) {
            return members;
        }
        
        // Remove hyphens from search term for phone number matching
        String cleanSearchTerm = searchTerm.replace("-", "");
        
        String sql = "SELECT * FROM members WHERE REPLACE(phone_number, '-', '') LIKE ? OR name LIKE ? ORDER BY total_spent DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + cleanSearchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, "%" + searchTerm + "%"); // Keep original for name search
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Member member = new Member(
                        rs.getString("phone_number"),
                        rs.getString("name"),
                        rs.getDouble("total_spent")
                    );
                    members.add(member);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return members;
    }
    
    // Update member
    public boolean updateMember(Member member) {
        Connection connection = getConnection();
        if (connection == null) {
            return false;
        }
        
        String sql = "UPDATE members SET name=?, total_spent=?, membership_level=?, discount_percent=? " +
                    "WHERE phone_number=?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setDouble(2, member.getTotalSpent());
            pstmt.setInt(3, member.getMembershipLevel());
            pstmt.setDouble(4, member.getDiscountPercent());
            pstmt.setString(5, member.getPhoneNumber());
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete member
    public boolean deleteMember(String phoneNumber) {
        Connection connection = getConnection();
        if (connection == null) {
            return false;
        }
        
        String sql = "DELETE FROM members WHERE phone_number = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, phoneNumber);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get members by level
    public List<Member> getMembersByLevel(int level) {
        List<Member> members = new ArrayList<>();
        Connection connection = getConnection();
        if (connection == null) {
            return members;
        }
        
        String sql = "SELECT * FROM members WHERE membership_level = ? ORDER BY total_spent DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, level);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Member member = new Member(
                        rs.getString("phone_number"),
                        rs.getString("name"),
                        rs.getDouble("total_spent")
                    );
                    members.add(member);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return members;
    }
    
    // Get total number of members
    public int getTotalMemberCount() {
        Connection connection = getConnection();
        if (connection == null) {
            return 0;
        }
        
        String sql = "SELECT COUNT(*) as count FROM members";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
}

