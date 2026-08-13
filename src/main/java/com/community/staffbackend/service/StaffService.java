package com.community.staffbackend.service;

import com.community.staffbackend.dto.request.StaffCreateRequestDto;
import com.community.staffbackend.dto.request.StaffStatusUpdateRequestDto;
import com.community.staffbackend.dto.request.StaffUpdateRequestDto;
import com.community.staffbackend.dto.response.StaffDirectoryResponseDto;
import com.community.staffbackend.dto.response.StaffProfileResponseDto;
import com.community.staffbackend.dto.response.StaffResponseDto;
import com.community.staffbackend.entity.StaffStatus;

import java.util.List;

public interface StaffService {
    StaffResponseDto createStaff(StaffCreateRequestDto requestDto);
    StaffResponseDto getStaffById(Long id);
    StaffProfileResponseDto getStaffProfile(Long id);
    List<StaffDirectoryResponseDto> getStaffDirectory(String query, String role, StaffStatus status);
    StaffResponseDto updateStaff(Long id, StaffUpdateRequestDto requestDto);
    StaffResponseDto updateStaffStatus(Long id, StaffStatusUpdateRequestDto requestDto);
}
