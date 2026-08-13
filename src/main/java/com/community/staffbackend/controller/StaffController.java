package com.community.staffbackend.controller;

import com.community.staffbackend.dto.request.StaffCreateRequestDto;
import com.community.staffbackend.dto.request.StaffStatusUpdateRequestDto;
import com.community.staffbackend.dto.request.StaffUpdateRequestDto;
import com.community.staffbackend.dto.response.StaffDirectoryResponseDto;
import com.community.staffbackend.dto.response.StaffProfileResponseDto;
import com.community.staffbackend.dto.response.StaffResponseDto;
import com.community.staffbackend.entity.StaffStatus;
import com.community.staffbackend.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public ResponseEntity<List<StaffDirectoryResponseDto>> getAllStaff(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) StaffStatus status) {
        List<StaffDirectoryResponseDto> staffList = staffService.getStaffDirectory(query, role, status);
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffProfileResponseDto> getStaffProfile(@PathVariable Long id) {
        StaffProfileResponseDto profile = staffService.getStaffProfile(id);
        return ResponseEntity.ok(profile);
    }

    @PostMapping
    public ResponseEntity<StaffResponseDto> createStaff(@Valid @RequestBody StaffCreateRequestDto requestDto) {
        StaffResponseDto createdStaff = staffService.createStaff(requestDto);
        return new ResponseEntity<>(createdStaff, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffResponseDto> updateStaff(
            @PathVariable Long id,
            @Valid @RequestBody StaffUpdateRequestDto requestDto) {
        StaffResponseDto updatedStaff = staffService.updateStaff(id, requestDto);
        return ResponseEntity.ok(updatedStaff);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<StaffResponseDto> updateStaffStatus(
            @PathVariable Long id,
            @Valid @RequestBody StaffStatusUpdateRequestDto requestDto) {
        StaffResponseDto updatedStaff = staffService.updateStaffStatus(id, requestDto);
        return ResponseEntity.ok(updatedStaff);
    }
}
