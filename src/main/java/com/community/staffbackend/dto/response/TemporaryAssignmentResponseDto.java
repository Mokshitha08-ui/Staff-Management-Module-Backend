package com.community.staffbackend.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TemporaryAssignmentResponseDto {

    private Long id;
    private String assignmentCode;
    private Long regularStaffId;
    private String regularStaffCode;
    private String regularStaffName;
    private String regularCategory;
    private Long replacementStaffId;
    private String replacementStaffCode;
    private String replacementStaffName;
    private String replacementPhone;
    private Boolean isTemporaryWorker;
    private String reason;
    private String towerAssigned;
    private String blockAssigned;
    private String shift;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isUntilFurtherNotice;
    private String status;
    private LocalDateTime createdAt;

    public TemporaryAssignmentResponseDto() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssignmentCode() {
        return assignmentCode;
    }

    public void setAssignmentCode(String assignmentCode) {
        this.assignmentCode = assignmentCode;
    }

    public Long getRegularStaffId() {
        return regularStaffId;
    }

    public void setRegularStaffId(Long regularStaffId) {
        this.regularStaffId = regularStaffId;
    }

    public String getRegularStaffCode() {
        return regularStaffCode;
    }

    public void setRegularStaffCode(String regularStaffCode) {
        this.regularStaffCode = regularStaffCode;
    }

    public String getRegularStaffName() {
        return regularStaffName;
    }

    public void setRegularStaffName(String regularStaffName) {
        this.regularStaffName = regularStaffName;
    }

    public String getRegularCategory() {
        return regularCategory;
    }

    public void setRegularCategory(String regularCategory) {
        this.regularCategory = regularCategory;
    }

    public Long getReplacementStaffId() {
        return replacementStaffId;
    }

    public void setReplacementStaffId(Long replacementStaffId) {
        this.replacementStaffId = replacementStaffId;
    }

    public String getReplacementStaffCode() {
        return replacementStaffCode;
    }

    public void setReplacementStaffCode(String replacementStaffCode) {
        this.replacementStaffCode = replacementStaffCode;
    }

    public String getReplacementStaffName() {
        return replacementStaffName;
    }

    public void setReplacementStaffName(String replacementStaffName) {
        this.replacementStaffName = replacementStaffName;
    }

    public String getReplacementPhone() {
        return replacementPhone;
    }

    public void setReplacementPhone(String replacementPhone) {
        this.replacementPhone = replacementPhone;
    }

    public Boolean getIsTemporaryWorker() {
        return isTemporaryWorker;
    }

    public void setIsTemporaryWorker(Boolean temporaryWorker) {
        isTemporaryWorker = temporaryWorker;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTowerAssigned() {
        return towerAssigned;
    }

    public void setTowerAssigned(String towerAssigned) {
        this.towerAssigned = towerAssigned;
    }

    public String getBlockAssigned() {
        return blockAssigned;
    }

    public void setBlockAssigned(String blockAssigned) {
        this.blockAssigned = blockAssigned;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsUntilFurtherNotice() {
        return isUntilFurtherNotice;
    }

    public void setIsUntilFurtherNotice(Boolean untilFurtherNotice) {
        isUntilFurtherNotice = untilFurtherNotice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
