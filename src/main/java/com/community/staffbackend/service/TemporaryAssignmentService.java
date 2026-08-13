package com.community.staffbackend.service;

import com.community.staffbackend.dto.request.TemporaryAssignmentCreateRequestDto;
import com.community.staffbackend.dto.request.TemporaryAssignmentUpdateRequestDto;
import com.community.staffbackend.dto.response.TemporaryAssignmentResponseDto;
import com.community.staffbackend.entity.ReplacementStatus;

import java.util.List;

public interface TemporaryAssignmentService {
    TemporaryAssignmentResponseDto createAssignment(TemporaryAssignmentCreateRequestDto requestDto);
    TemporaryAssignmentResponseDto getAssignmentById(Long id);
    List<TemporaryAssignmentResponseDto> getAssignments(ReplacementStatus status);
    TemporaryAssignmentResponseDto updateAssignment(Long id, TemporaryAssignmentUpdateRequestDto requestDto);
    void deleteAssignment(Long id);
}
