package com.community.staffbackend.service;

import com.community.staffbackend.dto.request.StaffCreateRequestDto;
import com.community.staffbackend.dto.request.StaffStatusUpdateRequestDto;
import com.community.staffbackend.dto.request.StaffUpdateRequestDto;
import com.community.staffbackend.dto.response.StaffProfileResponseDto;
import com.community.staffbackend.dto.response.StaffResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StaffService {
    List<StaffResponseDto> getAllStaff(String query, String category, String status);
    StaffResponseDto getStaffById(Long id);
    StaffResponseDto getStaffByStaffId(String staffId);
    StaffProfileResponseDto getStaffProfile(Long id);
    List<StaffResponseDto> getOnDutyStaff();
    List<StaffResponseDto> getPublicStaffList();
    StaffResponseDto getPublicStaffProfile(String staffId);
    List<StaffResponseDto> getDigitalStaffIds();

    StaffResponseDto createStaff(StaffCreateRequestDto requestDto);
    StaffResponseDto updateStaff(Long id, StaffUpdateRequestDto requestDto);
    StaffResponseDto updateStaffStatus(Long id, StaffStatusUpdateRequestDto requestDto);
    void deleteStaff(Long id);

    StaffResponseDto checkInStaff(Long id);
    StaffResponseDto checkOutStaff(Long id);
    StaffResponseDto updateVerificationStatus(Long id, String verificationStatus);
    StaffResponseDto markStaffOnLeave(Long id, LocalDateTime startDate, LocalDateTime endDate);
    StaffResponseDto suspendStaff(Long id);
    StaffResponseDto activateStaff(Long id);
    StaffResponseDto deactivateStaff(Long id);
    StaffResponseDto markUnavailable(Long id);
}
