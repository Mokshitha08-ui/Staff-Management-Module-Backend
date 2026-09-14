package com.community.staffbackend.controller;

import com.community.staffbackend.dto.request.StaffCreateRequestDto;
import com.community.staffbackend.dto.request.StaffStatusUpdateRequestDto;
import com.community.staffbackend.dto.request.StaffUpdateRequestDto;
import com.community.staffbackend.dto.response.DashboardResponseDto;
import com.community.staffbackend.dto.response.StaffProfileResponseDto;
import com.community.staffbackend.dto.response.StaffResponseDto;
import com.community.staffbackend.service.DashboardService;
import com.community.staffbackend.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;
    private final DashboardService dashboardService;

    public StaffController(StaffService staffService, DashboardService dashboardService) {
        this.staffService = staffService;
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<List<StaffResponseDto>> getAllStaff(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {
        List<StaffResponseDto> list = staffService.getAllStaff(query, category, status);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardResponseDto> getStaffSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardStatistics());
    }

    @GetMapping("/on-duty")
    public ResponseEntity<List<StaffResponseDto>> getOnDutyStaff() {
        return ResponseEntity.ok(staffService.getOnDutyStaff());
    }

    @GetMapping("/public")
    public ResponseEntity<List<StaffResponseDto>> getPublicStaffList() {
        return ResponseEntity.ok(staffService.getPublicStaffList());
    }

    @GetMapping("/public/{staffId}")
    public ResponseEntity<StaffResponseDto> getPublicStaffProfile(@PathVariable String staffId) {
        return ResponseEntity.ok(staffService.getPublicStaffProfile(staffId));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<StaffResponseDto>> getDigitalStaffIds() {
        return ResponseEntity.ok(staffService.getDigitalStaffIds());
    }

    @GetMapping("/ids/{id}")
    public ResponseEntity<StaffResponseDto> getDigitalStaffIdById(@PathVariable String id) {
        try {
            Long numericId = Long.parseLong(id);
            return ResponseEntity.ok(staffService.getStaffById(numericId));
        } catch (NumberFormatException e) {
            return ResponseEntity.ok(staffService.getStaffByStaffId(id));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffProfileResponseDto> getStaffProfile(@PathVariable String id) {
        try {
            Long numericId = Long.parseLong(id);
            return ResponseEntity.ok(staffService.getStaffProfile(numericId));
        } catch (NumberFormatException e) {
            StaffResponseDto dto = staffService.getStaffByStaffId(id);
            return ResponseEntity.ok(staffService.getStaffProfile(dto.getId()));
        }
    }

    @PostMapping
    public ResponseEntity<StaffResponseDto> createStaff(@Valid @RequestBody StaffCreateRequestDto requestDto) {
        StaffResponseDto created = staffService.createStaff(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffResponseDto> updateStaff(
            @PathVariable Long id,
            @RequestBody StaffUpdateRequestDto requestDto) {
        StaffResponseDto updated = staffService.updateStaff(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/{id}/check-in")
    public ResponseEntity<StaffResponseDto> checkInStaff(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.checkInStaff(id));
    }

    @PostMapping("/{id}/check-out")
    public ResponseEntity<StaffResponseDto> checkOutStaff(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.checkOutStaff(id));
    }

    @PatchMapping("/{id}/verification")
    public ResponseEntity<StaffResponseDto> updateVerificationStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        return ResponseEntity.ok(staffService.updateVerificationStatus(id, status));
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<StaffResponseDto> markStaffOnLeave(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        LocalDateTime start = (body != null && body.get("startDate") != null) ? LocalDateTime.parse(body.get("startDate")) : LocalDateTime.now();
        LocalDateTime end = (body != null && body.get("endDate") != null) ? LocalDateTime.parse(body.get("endDate")) : null;
        return ResponseEntity.ok(staffService.markStaffOnLeave(id, start, end));
    }

    @PostMapping("/{id}/suspend")
    public ResponseEntity<StaffResponseDto> suspendStaff(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.suspendStaff(id));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<StaffResponseDto> activateStaff(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.activateStaff(id));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<StaffResponseDto> deactivateStaff(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.deactivateStaff(id));
    }

    @PostMapping("/{id}/unavailable")
    public ResponseEntity<StaffResponseDto> markUnavailable(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.markUnavailable(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<StaffResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestBody StaffStatusUpdateRequestDto requestDto) {
        return ResponseEntity.ok(staffService.updateStaffStatus(id, requestDto));
    }
}
