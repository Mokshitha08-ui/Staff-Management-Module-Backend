package com.community.staffbackend.dto.response;

import com.community.staffbackend.entity.ReplacementStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TemporaryAssignmentResponseDto {

    private Long id;
    private Long regularStaffId;
    private String regularStaffName;
    private Long replacementStaffId;
    private String replacementStaffName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String assignedArea;
    private String reason;
    private ReplacementStatus status;
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

    public Long getRegularStaffId() {
        return regularStaffId;
    }

    public void setRegularStaffId(Long regularStaffId) {
        this.regularStaffId = regularStaffId;
    }

    public String getRegularStaffName() {
        return regularStaffName;
    }

    public void setRegularStaffName(String regularStaffName) {
        this.regularStaffName = regularStaffName;
    }

    public Long getReplacementStaffId() {
        return replacementStaffId;
    }

    public void setReplacementStaffId(Long replacementStaffId) {
        this.replacementStaffId = replacementStaffId;
    }

    public String getReplacementStaffName() {
        return replacementStaffName;
    }

    public void setReplacementStaffName(String replacementStaffName) {
        this.replacementStaffName = replacementStaffName;
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

    public String getAssignedArea() {
        return assignedArea;
    }

    public void setAssignedArea(String assignedArea) {
        this.assignedArea = assignedArea;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ReplacementStatus getStatus() {
        return status;
    }

    public void setStatus(ReplacementStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
