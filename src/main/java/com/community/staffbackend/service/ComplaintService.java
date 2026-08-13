package com.community.staffbackend.service;

import com.community.staffbackend.dto.request.ComplaintCreateRequestDto;
import com.community.staffbackend.dto.request.ComplaintStatusUpdateRequestDto;
import com.community.staffbackend.dto.response.ComplaintResponseDto;
import com.community.staffbackend.entity.ComplaintStatus;

import java.time.LocalDate;
import java.util.List;

public interface ComplaintService {
    ComplaintResponseDto createComplaint(ComplaintCreateRequestDto requestDto);
    ComplaintResponseDto getComplaintById(Long id);
    List<ComplaintResponseDto> getComplaints(Long staffId, ComplaintStatus status, LocalDate date);
    ComplaintResponseDto updateComplaintStatus(Long id, ComplaintStatusUpdateRequestDto requestDto);
}
