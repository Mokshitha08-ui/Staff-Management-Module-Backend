package com.community.staffbackend.controller;

import com.community.staffbackend.dto.request.ComplaintCreateRequestDto;
import com.community.staffbackend.dto.request.ComplaintStatusUpdateRequestDto;
import com.community.staffbackend.dto.response.ComplaintResponseDto;
import com.community.staffbackend.entity.ComplaintStatus;
import com.community.staffbackend.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping
    public ResponseEntity<List<ComplaintResponseDto>> getComplaints(
            @RequestParam(required = false) Long staffId,
            @RequestParam(required = false) ComplaintStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ComplaintResponseDto> complaints = complaintService.getComplaints(staffId, status, date);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponseDto> getComplaintById(@PathVariable Long id) {
        ComplaintResponseDto complaint = complaintService.getComplaintById(id);
        return ResponseEntity.ok(complaint);
    }

    @PostMapping
    public ResponseEntity<ComplaintResponseDto> createComplaint(@Valid @RequestBody ComplaintCreateRequestDto requestDto) {
        ComplaintResponseDto created = complaintService.createComplaint(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ComplaintResponseDto> updateComplaintStatus(
            @PathVariable Long id,
            @Valid @RequestBody ComplaintStatusUpdateRequestDto requestDto) {
        ComplaintResponseDto updated = complaintService.updateComplaintStatus(id, requestDto);
        return ResponseEntity.ok(updated);
    }
}
