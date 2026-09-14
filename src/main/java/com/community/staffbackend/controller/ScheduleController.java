package com.community.staffbackend.controller;

import com.community.staffbackend.dto.request.ScheduleCreateRequestDto;
import com.community.staffbackend.dto.request.ScheduleUpdateRequestDto;
import com.community.staffbackend.dto.response.ScheduleResponseDto;
import com.community.staffbackend.dto.response.StaffResponseDto;
import com.community.staffbackend.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getSchedules(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String staffId,
            @RequestParam(required = false) String shift) {
        List<ScheduleResponseDto> schedules = scheduleService.getSchedules(date, staffId, shift);
        return ResponseEntity.ok(schedules);
    }

    @PostMapping("/assign-shift")
    public ResponseEntity<ScheduleResponseDto> assignShift(
            @RequestParam String staffId,
            @RequestBody Map<String, Object> shiftData) {
        return ResponseEntity.ok(scheduleService.assignShift(staffId, shiftData));
    }

    @PostMapping("/assign-area")
    public ResponseEntity<StaffResponseDto> assignArea(
            @RequestParam String staffId,
            @RequestBody Object areaData) {
        return ResponseEntity.ok(scheduleService.assignArea(staffId, areaData));
    }

    @GetMapping("/leaves")
    public ResponseEntity<List<Map<String, Object>>> getLeaveSchedule() {
        return ResponseEntity.ok(scheduleService.getLeaveSchedule());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getScheduleById(@PathVariable Long id) {
        ScheduleResponseDto schedule = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleCreateRequestDto requestDto) {
        ScheduleResponseDto created = scheduleService.createSchedule(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleUpdateRequestDto requestDto) {
        ScheduleResponseDto updated = scheduleService.updateSchedule(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
