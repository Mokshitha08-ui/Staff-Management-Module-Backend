package com.community.staffbackend.service.impl;

import com.community.staffbackend.dto.request.TemporaryAssignmentCreateRequestDto;
import com.community.staffbackend.dto.request.TemporaryAssignmentUpdateRequestDto;
import com.community.staffbackend.dto.response.TemporaryAssignmentResponseDto;
import com.community.staffbackend.entity.ReplacementStatus;
import com.community.staffbackend.entity.Staff;
import com.community.staffbackend.entity.TemporaryAssignment;
import com.community.staffbackend.exception.InvalidOperationException;
import com.community.staffbackend.exception.ResourceNotFoundException;
import com.community.staffbackend.repository.StaffRepository;
import com.community.staffbackend.repository.TemporaryAssignmentRepository;
import com.community.staffbackend.service.TemporaryAssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TemporaryAssignmentServiceImpl implements TemporaryAssignmentService {

    private final TemporaryAssignmentRepository assignmentRepository;
    private final StaffRepository staffRepository;

    public TemporaryAssignmentServiceImpl(TemporaryAssignmentRepository assignmentRepository, StaffRepository staffRepository) {
        this.assignmentRepository = assignmentRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public TemporaryAssignmentResponseDto createAssignment(TemporaryAssignmentCreateRequestDto requestDto) {
        if (requestDto.getRegularStaffId().equals(requestDto.getReplacementStaffId())) {
            throw new InvalidOperationException("Regular staff and replacement staff cannot be the same person");
        }

        if (requestDto.getStartDate().isAfter(requestDto.getEndDate())) {
            throw new InvalidOperationException("Start date (" + requestDto.getStartDate() + ") cannot be after end date (" + requestDto.getEndDate() + ")");
        }

        Staff regularStaff = staffRepository.findById(requestDto.getRegularStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Regular staff member not found with ID: " + requestDto.getRegularStaffId()));

        Staff replacementStaff = staffRepository.findById(requestDto.getReplacementStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Replacement staff member not found with ID: " + requestDto.getReplacementStaffId()));

        TemporaryAssignment assignment = new TemporaryAssignment();
        assignment.setRegularStaff(regularStaff);
        assignment.setReplacementStaff(replacementStaff);
        assignment.setStartDate(requestDto.getStartDate());
        assignment.setEndDate(requestDto.getEndDate());
        assignment.setAssignedArea(requestDto.getAssignedArea());
        assignment.setReason(requestDto.getReason());
        assignment.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : ReplacementStatus.ACTIVE);

        TemporaryAssignment saved = assignmentRepository.save(assignment);
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TemporaryAssignmentResponseDto getAssignmentById(Long id) {
        TemporaryAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Temporary worker assignment not found with ID: " + id));
        return mapToResponseDto(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TemporaryAssignmentResponseDto> getAssignments(ReplacementStatus status) {
        List<TemporaryAssignment> list = status != null ?
                assignmentRepository.findByStatus(status) : assignmentRepository.findAll();
        return list.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Override
    public TemporaryAssignmentResponseDto updateAssignment(Long id, TemporaryAssignmentUpdateRequestDto requestDto) {
        TemporaryAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Temporary worker assignment not found with ID: " + id));

        if (requestDto.getStartDate() != null) assignment.setStartDate(requestDto.getStartDate());
        if (requestDto.getEndDate() != null) assignment.setEndDate(requestDto.getEndDate());
        if (assignment.getStartDate().isAfter(assignment.getEndDate())) {
            throw new InvalidOperationException("Start date cannot be after end date");
        }

        if (requestDto.getAssignedArea() != null) assignment.setAssignedArea(requestDto.getAssignedArea());
        if (requestDto.getReason() != null) assignment.setReason(requestDto.getReason());
        if (requestDto.getStatus() != null) assignment.setStatus(requestDto.getStatus());

        TemporaryAssignment updated = assignmentRepository.save(assignment);
        return mapToResponseDto(updated);
    }

    @Override
    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Temporary worker assignment not found with ID: " + id);
        }
        assignmentRepository.deleteById(id);
    }

    private TemporaryAssignmentResponseDto mapToResponseDto(TemporaryAssignment assignment) {
        TemporaryAssignmentResponseDto dto = new TemporaryAssignmentResponseDto();
        dto.setId(assignment.getId());
        dto.setRegularStaffId(assignment.getRegularStaff().getId());
        dto.setRegularStaffName(assignment.getRegularStaff().getFullName());
        dto.setReplacementStaffId(assignment.getReplacementStaff().getId());
        dto.setReplacementStaffName(assignment.getReplacementStaff().getFullName());
        dto.setStartDate(assignment.getStartDate());
        dto.setEndDate(assignment.getEndDate());
        dto.setAssignedArea(assignment.getAssignedArea());
        dto.setReason(assignment.getReason());
        dto.setStatus(assignment.getStatus());
        dto.setCreatedAt(assignment.getCreatedAt());
        return dto;
    }
}
