package com.community.staffbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff_member")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "staff_id", unique = true, nullable = false)
    private String staffId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "photo")
    private String photo;

    @Column(name = "category")
    private String category;

    @Column(name = "role")
    private String role;

    @Column(name = "block_assigned")
    private String blockAssigned;

    @Column(name = "tower_assigned")
    private String towerAssigned;

    @Column(name = "status", nullable = false)
    private String status; // active, inactive, on_leave, suspended, unavailable

    @Column(name = "verification_status")
    private String verificationStatus; // pending, under_review, approved, rejected

    @Column(name = "verification_submitted_date")
    private LocalDateTime verificationSubmittedDate;

    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @Column(name = "document_expiry")
    private LocalDateTime documentExpiry;

    @Column(name = "is_inside_community")
    private Boolean isInsideCommunity = false;

    @Column(name = "last_check_in")
    private LocalDateTime lastCheckIn;

    @Column(name = "last_check_out")
    private LocalDateTime lastCheckOut;

    @Column(name = "leave_start_date")
    private LocalDateTime leaveStartDate;

    @Column(name = "leave_end_date")
    private LocalDateTime leaveEndDate;

    @Column(name = "is_temporary")
    private Boolean isTemporary = false;

    @Column(name = "skills")
    private String skills;

    @Column(name = "experience")
    private String experience;

    @Column(name = "working_hours")
    private String workingHours;

    @Column(name = "address")
    private String address;

    @Column(name = "availability")
    private String availability;

    @Column(name = "id_proof_type")
    private String idProofType;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_relation")
    private String emergencyContactRelation;

    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Staff() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "active";
        }
        if (this.verificationStatus == null) {
            this.verificationStatus = "approved";
        }
        if (this.isInsideCommunity == null) {
            this.isInsideCommunity = false;
        }
        if (this.isTemporary == null) {
            this.isTemporary = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBlockAssigned() {
        return blockAssigned;
    }

    public void setBlockAssigned(String blockAssigned) {
        this.blockAssigned = blockAssigned;
    }

    public String getTowerAssigned() {
        return towerAssigned;
    }

    public void setTowerAssigned(String towerAssigned) {
        this.towerAssigned = towerAssigned;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public LocalDateTime getVerificationSubmittedDate() {
        return verificationSubmittedDate;
    }

    public void setVerificationSubmittedDate(LocalDateTime verificationSubmittedDate) {
        this.verificationSubmittedDate = verificationSubmittedDate;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public LocalDateTime getDocumentExpiry() {
        return documentExpiry;
    }

    public void setDocumentExpiry(LocalDateTime documentExpiry) {
        this.documentExpiry = documentExpiry;
    }

    public Boolean getIsInsideCommunity() {
        return isInsideCommunity;
    }

    public void setIsInsideCommunity(Boolean insideCommunity) {
        isInsideCommunity = insideCommunity;
    }

    public LocalDateTime getLastCheckIn() {
        return lastCheckIn;
    }

    public void setLastCheckIn(LocalDateTime lastCheckIn) {
        this.lastCheckIn = lastCheckIn;
    }

    public LocalDateTime getLastCheckOut() {
        return lastCheckOut;
    }

    public void setLastCheckOut(LocalDateTime lastCheckOut) {
        this.lastCheckOut = lastCheckOut;
    }

    public LocalDateTime getLeaveStartDate() {
        return leaveStartDate;
    }

    public void setLeaveStartDate(LocalDateTime leaveStartDate) {
        this.leaveStartDate = leaveStartDate;
    }

    public LocalDateTime getLeaveEndDate() {
        return leaveEndDate;
    }

    public void setLeaveEndDate(LocalDateTime leaveEndDate) {
        this.leaveEndDate = leaveEndDate;
    }

    public Boolean getIsTemporary() {
        return isTemporary;
    }

    public void setIsTemporary(Boolean temporary) {
        isTemporary = temporary;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getIdProofType() {
        return idProofType;
    }

    public void setIdProofType(String idProofType) {
        this.idProofType = idProofType;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactRelation() {
        return emergencyContactRelation;
    }

    public void setEmergencyContactRelation(String emergencyContactRelation) {
        this.emergencyContactRelation = emergencyContactRelation;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
