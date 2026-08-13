package com.community.staffbackend.service.impl;

import com.community.staffbackend.dto.request.ComplaintCreateRequestDto;
import com.community.staffbackend.dto.request.ComplaintStatusUpdateRequestDto;
import com.community.staffbackend.dto.response.ComplaintResponseDto;
import com.community.staffbackend.entity.Complaint;
import com.community.staffbackend.entity.ComplaintStatus;
import com.community.staffbackend.entity.Staff;
import com.community.staffbackend.exception.ResourceNotFoundException;
import com.community.staffbackend.repository.ComplaintRepository;
import com.community.staffbackend.repository.StaffRepository;
import com.community.staffbackend.service.ComplaintService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final StaffRepository staffRepository;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository, StaffRepository staffRepository) {
        this.complaintRepository = complaintRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public ComplaintResponseDto createComplaint(ComplaintCreateRequestDto requestDto) {
        Staff staff = staffRepository.findById(requestDto.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + requestDto.getStaffId()));

        Complaint complaint = new Complaint();
        complaint.setStaff(staff);
        complaint.setComplainantName(requestDto.getComplainantName() != null ? requestDto.getComplainantName() : "Anonymous");
        complaint.setComplaintText(requestDto.getComplaintText());
        complaint.setStatus(ComplaintStatus.PENDING);

        Complaint saved = complaintRepository.save(complaint);
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ComplaintResponseDto getComplaintById(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with ID: " + id));
        return mapToResponseDto(complaint);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplaintResponseDto> getComplaints(Long staffId, ComplaintStatus status, LocalDate date) {
        List<Complaint> list = complaintRepository.searchComplaints(staffId, status, date);
        return list.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Override
    public ComplaintResponseDto updateComplaintStatus(Long id, ComplaintStatusUpdateRequestDto requestDto) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with ID: " + id));

        complaint.setStatus(requestDto.getStatus());
        if (requestDto.getResolution() != null) {
            complaint.setResolution(requestDto.getResolution());
        }
        if (requestDto.getStatus() == ComplaintStatus.RESOLVED || requestDto.getStatus() == ComplaintStatus.REJECTED) {
            complaint.setResolvedAt(LocalDateTime.now());
        }

        Complaint updated = complaintRepository.save(complaint);
        return mapToResponseDto(updated);
    }

    private ComplaintResponseDto mapToResponseDto(Complaint complaint) {
        ComplaintResponseDto dto = new ComplaintResponseDto();
        dto.setId(complaint.getId());
        dto.setStaffId(complaint.getStaff().getId());
        dto.setStaffName(complaint.getStaff().getFullName());
        dto.setComplainantName(complaint.getComplainantName());
        dto.setComplaintText(complaint.getComplaintText());
        dto.setComplaintDate(complaint.getComplaintDate());
        dto.setStatus(complaint.getStatus());
        dto.setResolution(complaint.getResolution());
        dto.setResolvedAt(complaint.getResolvedAt());
        return dto;
    }
}
