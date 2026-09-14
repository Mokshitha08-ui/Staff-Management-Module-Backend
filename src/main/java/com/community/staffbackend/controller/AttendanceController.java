package com.community.staffbackend.controller;

import com.community.staffbackend.dto.request.AttendanceCheckInRequestDto;
import com.community.staffbackend.dto.request.AttendanceCheckOutRequestDto;
import com.community.staffbackend.dto.response.AttendanceResponseDto;
import com.community.staffbackend.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponseDto> checkIn(@Valid @RequestBody AttendanceCheckInRequestDto requestDto) {
        AttendanceResponseDto response = attendanceService.checkIn(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/check-out")
    public ResponseEntity<AttendanceResponseDto> checkOut(@Valid @RequestBody AttendanceCheckOutRequestDto requestDto) {
        AttendanceResponseDto response = attendanceService.checkOut(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AttendanceResponseDto>> getAttendanceRecords(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long staffId,
            @RequestParam(required = false) String status) {
        List<AttendanceResponseDto> records = attendanceService.getAttendanceRecords(date, staffId, status);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/history")
    public ResponseEntity<List<AttendanceResponseDto>> getAttendanceHistory(
            @RequestParam(required = false) String staffId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
        List<AttendanceResponseDto> history = attendanceService.getAttendanceHistory(staffId, date, status, search);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/logs")
    public ResponseEntity<List<Map<String, Object>>> getEntryExitLogs() {
        return ResponseEntity.ok(attendanceService.getEntryExitLogs());
    }

    @GetMapping("/feed")
    public ResponseEntity<List<Map<String, Object>>> getActivityFeed() {
        return ResponseEntity.ok(attendanceService.getActivityFeed());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceResponseDto> getAttendanceById(@PathVariable Long id) {
        AttendanceResponseDto response = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(response);
    }
}
