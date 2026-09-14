package com.community.staffbackend.service;

import com.community.staffbackend.dto.request.TemporaryAssignmentCreateRequestDto;
import com.community.staffbackend.dto.request.TemporaryAssignmentUpdateRequestDto;
import com.community.staffbackend.dto.response.TemporaryAssignmentResponseDto;

import java.util.List;
import java.util.Map;

public interface TemporaryAssignmentService {
    TemporaryAssignmentResponseDto createAssignment(TemporaryAssignmentCreateRequestDto requestDto);
    TemporaryAssignmentResponseDto assignReplacement(Map<String, Object> body);
    TemporaryAssignmentResponseDto getAssignmentById(Long id);
    List<TemporaryAssignmentResponseDto> getAssignments(String status);
    List<TemporaryAssignmentResponseDto> getActiveReplacements();
    List<TemporaryAssignmentResponseDto> getReplacementHistory(String search, String status);
    TemporaryAssignmentResponseDto updateAssignment(Long id, TemporaryAssignmentUpdateRequestDto requestDto);
    void deleteAssignment(Long id);
}
