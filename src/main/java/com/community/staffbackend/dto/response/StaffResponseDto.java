package com.community.staffbackend.dto.response;

import java.time.LocalDateTime;

public class StaffResponseDto {

    private Long id;
    private String staffId;
    private String name;
    private String fullName;
    private String phone;
    private String email;
    private String photo;
    private String category;
    private String role;
    private String blockAssigned;
    private String towerAssigned;
    private String status;
    private String verificationStatus;
    private LocalDateTime verificationSubmittedDate;
    private LocalDateTime joinDate;
    private LocalDateTime documentExpiry;
    private Boolean isInsideCommunity;
    private LocalDateTime lastCheckIn;
    private LocalDateTime lastCheckOut;
    private LocalDateTime leaveStartDate;
    private LocalDateTime leaveEndDate;
    private Boolean isTemporary;
    private String skills;
    private String experience;
    private String workingHours;
    private String address;
    private String availability;
    private String idProofType;
    private String emergencyContactName;
    private String emergencyContactRelation;
    private String emergencyContactPhone;
    private EmergencyContactDto emergencyContact;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static class EmergencyContactDto {
        private String name;
        private String relation;
        private String phone;

        public EmergencyContactDto() {}
        public EmergencyContactDto(String name, String relation, String phone) {
            this.name = name;
            this.relation = relation;
            this.phone = phone;
        }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRelation() { return relation; }
        public void setRelation(String relation) { this.relation = relation; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }

    public StaffResponseDto() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        this.name = fullName;
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
        updateEmergencyContactDto();
    }

    public String getEmergencyContactRelation() {
        return emergencyContactRelation;
    }

    public void setEmergencyContactRelation(String emergencyContactRelation) {
        this.emergencyContactRelation = emergencyContactRelation;
        updateEmergencyContactDto();
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
        updateEmergencyContactDto();
    }

    public EmergencyContactDto getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(EmergencyContactDto emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    private void updateEmergencyContactDto() {
        if (this.emergencyContactName != null || this.emergencyContactPhone != null) {
            this.emergencyContact = new EmergencyContactDto(this.emergencyContactName, this.emergencyContactRelation, this.emergencyContactPhone);
        }
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
