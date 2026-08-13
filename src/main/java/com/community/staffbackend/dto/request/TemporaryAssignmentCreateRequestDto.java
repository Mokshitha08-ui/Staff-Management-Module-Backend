package com.community.staffbackend.dto.request;

import com.community.staffbackend.entity.ReplacementStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class TemporaryAssignmentCreateRequestDto {

    @NotNull(message = "Regular staff ID is required")
    private Long regularStaffId;

    @NotNull(message = "Replacement staff ID is required")
    private Long replacementStaffId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String assignedArea;

    private String reason;

    private ReplacementStatus status;

    public TemporaryAssignmentCreateRequestDto() {
    }

    // Getters and Setters
    public Long getRegularStaffId() {
        return regularStaffId;
    }

    public void setRegularStaffId(Long regularStaffId) {
        this.regularStaffId = regularStaffId;
    }

    public Long getReplacementStaffId() {
        return replacementStaffId;
    }

    public void setReplacementStaffId(Long replacementStaffId) {
        this.replacementStaffId = replacementStaffId;
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
}
