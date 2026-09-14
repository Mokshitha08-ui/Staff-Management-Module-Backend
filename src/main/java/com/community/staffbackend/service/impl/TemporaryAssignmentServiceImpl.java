package com.community.staffbackend.service.impl;

import com.community.staffbackend.dto.request.TemporaryAssignmentCreateRequestDto;
import com.community.staffbackend.dto.request.TemporaryAssignmentUpdateRequestDto;
import com.community.staffbackend.dto.response.TemporaryAssignmentResponseDto;
import com.community.staffbackend.entity.Staff;
import com.community.staffbackend.entity.TemporaryAssignment;
import com.community.staffbackend.exception.ResourceNotFoundException;
import com.community.staffbackend.repository.StaffRepository;
import com.community.staffbackend.repository.TemporaryAssignmentRepository;
import com.community.staffbackend.service.TemporaryAssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
        Staff regularStaff = staffRepository.findById(requestDto.getRegularStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Regular staff not found with ID: " + requestDto.getRegularStaffId()));

        Staff replacementStaff = staffRepository.findById(requestDto.getReplacementStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Replacement staff not found with ID: " + requestDto.getReplacementStaffId()));

        TemporaryAssignment assignment = new TemporaryAssignment();
        assignment.setRegularStaff(regularStaff);
        assignment.setReplacementStaff(replacementStaff);
        assignment.setStartDate(requestDto.getStartDate() != null ? requestDto.getStartDate() : LocalDate.now());
        assignment.setEndDate(requestDto.getEndDate());
        assignment.setTowerAssigned(requestDto.getAssignedArea() != null ? requestDto.getAssignedArea() : regularStaff.getTowerAssigned());
        assignment.setBlockAssigned(regularStaff.getBlockAssigned());
        assignment.setReason(requestDto.getReason());
        assignment.setStatus("active");

        regularStaff.setStatus("on_leave");
        staffRepository.save(regularStaff);

        TemporaryAssignment saved = assignmentRepository.save(assignment);
        return mapToResponseDto(saved);
    }

    @Override
    public TemporaryAssignmentResponseDto assignReplacement(Map<String, Object> body) {
        String regularIdStr = body.get("regularStaffId").toString();
        Staff regularStaff = findStaff(regularIdStr);

        Staff replacementStaff;
        boolean isNewWorker = body.containsKey("isNewWorker") && Boolean.parseBoolean(body.get("isNewWorker").toString());

        if (isNewWorker && body.get("tempWorker") instanceof Map) {
            Map<?, ?> temp = (Map<?, ?>) body.get("tempWorker");
            replacementStaff = new Staff();
            replacementStaff.setStaffId("STF-TEMP-" + String.format("%04d", System.currentTimeMillis() % 10000));
            replacementStaff.setFullName(temp.get("name") != null ? temp.get("name").toString() + " (Temp)" : "Temporary Worker");
            replacementStaff.setPhone(temp.get("phone") != null ? temp.get("phone").toString() : "+919000000000");
            replacementStaff.setCategory(temp.get("category") != null ? temp.get("category").toString() : regularStaff.getCategory());
            replacementStaff.setRole(replacementStaff.getCategory());
            replacementStaff.setTowerAssigned(body.get("towerAssigned") != null ? body.get("towerAssigned").toString() : regularStaff.getTowerAssigned());
            replacementStaff.setStatus("active");
            replacementStaff.setIsTemporary(true);
            replacementStaff.setJoinDate(LocalDateTime.now());
            replacementStaff = staffRepository.save(replacementStaff);
        } else {
            String repIdStr = body.get("replacementStaffId").toString();
            replacementStaff = findStaff(repIdStr);
        }

        regularStaff.setStatus("on_leave");
        staffRepository.save(regularStaff);

        TemporaryAssignment assignment = new TemporaryAssignment();
        assignment.setAssignmentCode("REP-" + String.format("%03d", assignmentRepository.count() + 1));
        assignment.setRegularStaff(regularStaff);
        assignment.setReplacementStaff(replacementStaff);
        assignment.setReplacementPhone(body.get("replacementPhone") != null ? body.get("replacementPhone").toString() : replacementStaff.getPhone());
        assignment.setIsTemporaryWorker(isNewWorker || (body.get("isTemporaryWorker") != null && Boolean.parseBoolean(body.get("isTemporaryWorker").toString())));
        assignment.setReason(body.get("reason") != null ? body.get("reason").toString() : "Temporary Coverage");
        assignment.setTowerAssigned(body.get("towerAssigned") != null ? body.get("towerAssigned").toString() : regularStaff.getTowerAssigned());
        assignment.setBlockAssigned(body.get("blockAssigned") != null ? body.get("blockAssigned").toString() : regularStaff.getBlockAssigned());
        assignment.setShift(body.get("shift") != null ? body.get("shift").toString() : "morning");
        assignment.setStartDate(body.get("startDate") != null ? LocalDate.parse(body.get("startDate").toString()) : LocalDate.now());
        if (body.get("endDate") != null && !body.get("endDate").toString().isEmpty() && !"Until Notice".equalsIgnoreCase(body.get("endDate").toString())) {
            assignment.setEndDate(LocalDate.parse(body.get("endDate").toString()));
        }
        assignment.setIsUntilFurtherNotice(body.get("isUntilFurtherNotice") != null && Boolean.parseBoolean(body.get("isUntilFurtherNotice").toString()));
        assignment.setStatus("active");

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
    public List<TemporaryAssignmentResponseDto> getAssignments(String status) {
        List<TemporaryAssignment> list = status != null ?
                assignmentRepository.findByStatus(status) : assignmentRepository.findAll();
        return list.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TemporaryAssignmentResponseDto> getActiveReplacements() {
        return getAssignments("active");
    }

    @Override
    @Transactional(readOnly = true)
    public List<TemporaryAssignmentResponseDto> getReplacementHistory(String search, String status) {
        List<TemporaryAssignment> list = assignmentRepository.searchAssignments(
                (search != null && !search.trim().isEmpty()) ? search.trim() : null,
                (status != null && !status.trim().isEmpty()) ? status.trim() : null);
        return list.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Override
    public TemporaryAssignmentResponseDto updateAssignment(Long id, TemporaryAssignmentUpdateRequestDto requestDto) {
        TemporaryAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Temporary worker assignment not found with ID: " + id));

        if (requestDto.getStartDate() != null) assignment.setStartDate(requestDto.getStartDate());
        if (requestDto.getEndDate() != null) assignment.setEndDate(requestDto.getEndDate());
        if (requestDto.getAssignedArea() != null) assignment.setTowerAssigned(requestDto.getAssignedArea());
        if (requestDto.getReason() != null) assignment.setReason(requestDto.getReason());
        if (requestDto.getStatus() != null) assignment.setStatus(requestDto.getStatus().name().toLowerCase());

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

    private Staff findStaff(String staffId) {
        try {
            Long id = Long.parseLong(staffId);
            return staffRepository.findById(id).orElseGet(() -> staffRepository.findByStaffId(staffId)
                    .orElseThrow(() -> new ResourceNotFoundException("Staff member not found: " + staffId)));
        } catch (NumberFormatException e) {
            return staffRepository.findByStaffId(staffId)
                    .orElseThrow(() -> new ResourceNotFoundException("Staff member not found: " + staffId));
        }
    }

    private TemporaryAssignmentResponseDto mapToResponseDto(TemporaryAssignment a) {
        TemporaryAssignmentResponseDto dto = new TemporaryAssignmentResponseDto();
        dto.setId(a.getId());
        dto.setAssignmentCode(a.getAssignmentCode() != null ? a.getAssignmentCode() : "REP-" + a.getId());
        dto.setRegularStaffId(a.getRegularStaff().getId());
        dto.setRegularStaffCode(a.getRegularStaff().getStaffId());
        dto.setRegularStaffName(a.getRegularStaff().getFullName());
        dto.setRegularCategory(a.getRegularStaff().getCategory());
        dto.setReplacementStaffId(a.getReplacementStaff().getId());
        dto.setReplacementStaffCode(a.getReplacementStaff().getStaffId());
        dto.setReplacementStaffName(a.getReplacementStaff().getFullName());
        dto.setReplacementPhone(a.getReplacementPhone() != null ? a.getReplacementPhone() : a.getReplacementStaff().getPhone());
        dto.setIsTemporaryWorker(a.getIsTemporaryWorker());
        dto.setReason(a.getReason());
        dto.setTowerAssigned(a.getTowerAssigned() != null ? a.getTowerAssigned() : a.getRegularStaff().getTowerAssigned());
        dto.setBlockAssigned(a.getBlockAssigned() != null ? a.getBlockAssigned() : a.getRegularStaff().getBlockAssigned());
        dto.setShift(a.getShift() != null ? a.getShift() : "morning");
        dto.setStartDate(a.getStartDate());
        dto.setEndDate(a.getEndDate());
        dto.setIsUntilFurtherNotice(a.getIsUntilFurtherNotice());
        dto.setStatus(a.getStatus());
        dto.setCreatedAt(a.getCreatedAt());
        return dto;
    }
}
