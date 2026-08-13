package com.community.staffbackend.dto.request;

import com.community.staffbackend.entity.ReplacementStatus;
import java.time.LocalDate;

public class TemporaryAssignmentUpdateRequestDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private String assignedArea;
    private String reason;
    private ReplacementStatus status;

    public TemporaryAssignmentUpdateRequestDto() {
    }

    // Getters and Setters
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
