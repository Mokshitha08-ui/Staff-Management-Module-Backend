package com.community.staffbackend.dto.request;

public class StaffStatusUpdateRequestDto {

    private String status;

    public StaffStatusUpdateRequestDto() {
    }

    public StaffStatusUpdateRequestDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
