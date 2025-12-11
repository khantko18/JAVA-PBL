package controller;

import model.Member;
import database.MemberDAO;
import java.util.List;

/**
 * Controller for Membership business logic
 */
public class MembershipController {
    private MemberDAO memberDAO;
    
    public MembershipController() {
        this.memberDAO = new MemberDAO();
    }
    
    public boolean addMember(String phoneNumber, String name) {
        return addMember(phoneNumber, name, 0.0);
    }
    
    public boolean addMember(String phoneNumber, String name, double totalSpent) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            System.err.println("Phone number cannot be empty");
            return false;
        }
        
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Name cannot be empty");
            return false;
        }
        
        Member member = new Member(phoneNumber.trim(), name.trim(), totalSpent);
        return memberDAO.insertMember(member);
    }
    
    public Member getMemberByPhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return null;
        }
        return memberDAO.getMemberByPhone(phoneNumber.trim());
    }
    
    public List<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }
    
    public List<Member> searchMembers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllMembers();
        }
        return memberDAO.searchMembers(searchTerm.trim());
    }
    
    public boolean updateMember(String oldPhoneNumber, String newPhoneNumber, String name, double totalSpent) {
        Member member = memberDAO.getMemberByPhone(oldPhoneNumber);
        if (member == null) {
            System.err.println("Member not found: " + oldPhoneNumber);
            return false;
        }
        
        if (!oldPhoneNumber.equals(newPhoneNumber)) {
            if (memberDAO.getMemberByPhone(newPhoneNumber) != null) {
                System.err.println("Phone number already exists: " + newPhoneNumber);
                return false;
            }
            
            memberDAO.deleteMember(oldPhoneNumber);
            Member newMember = new Member(newPhoneNumber, name, totalSpent);
            return memberDAO.insertMember(newMember);
        } else {
            member.setName(name);
            member.setTotalSpent(totalSpent);
            return memberDAO.updateMember(member);
        }
    }
    
    public boolean deleteMember(String phoneNumber) {
        return memberDAO.deleteMember(phoneNumber);
    }
    
    public boolean applyPaymentToMember(String phoneNumber, double finalAmount) {
        Member member = getMemberByPhone(phoneNumber);
        
        if (member == null) {
            return false;
        }
        
        member.addSpending(finalAmount);
        return memberDAO.updateMember(member);
    }
    
    public int getTotalMemberCount() {
        return memberDAO.getTotalMemberCount();
    }
    
    public List<Member> getMembersByLevel(int level) {
        return memberDAO.getMembersByLevel(level);
    }
<<<<<<< HEAD
}
=======
}

>>>>>>> 55a700e2030741b882993273b0411ca7dd52da67
