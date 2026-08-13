package com.community.staffbackend.dto.request;

import com.community.staffbackend.entity.ComplaintStatus;
import jakarta.validation.constraints.NotNull;

public class ComplaintStatusUpdateRequestDto {

    @NotNull(message = "Status is required")
    private ComplaintStatus status;

    private String resolution;

    public ComplaintStatusUpdateRequestDto() {
    }

    // Getters and Setters
    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
