package com.community.staffbackend.dto.request;

import com.community.staffbackend.entity.StaffStatus;
import jakarta.validation.constraints.NotNull;

public class StaffStatusUpdateRequestDto {

    @NotNull(message = "Status is required")
    private StaffStatus status;

    public StaffStatusUpdateRequestDto() {
    }

    public StaffStatusUpdateRequestDto(StaffStatus status) {
        this.status = status;
    }

    public StaffStatus getStatus() {
        return status;
    }

    public void setStatus(StaffStatus status) {
        this.status = status;
    }
}
