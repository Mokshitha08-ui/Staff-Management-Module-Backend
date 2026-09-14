package com.community.staffbackend.controller;

import com.community.staffbackend.service.ReportsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @GetMapping("/attendance-stats")
    public ResponseEntity<Map<String, Object>> getAttendanceStats(@RequestParam(defaultValue = "month") String range) {
        return ResponseEntity.ok(reportsService.getAttendanceStats(range));
    }

    @GetMapping("/staff-by-role")
    public ResponseEntity<List<Map<String, Object>>> getStaffByRole() {
        return ResponseEntity.ok(reportsService.getStaffByRole());
    }

    @GetMapping("/staff-by-block")
    public ResponseEntity<List<Map<String, Object>>> getStaffByBlock() {
        return ResponseEntity.ok(reportsService.getStaffByBlock());
    }

    @GetMapping("/leave-stats")
    public ResponseEntity<Map<String, Object>> getLeaveStats() {
        return ResponseEntity.ok(reportsService.getLeaveStats());
    }

    @GetMapping("/replacement-stats")
    public ResponseEntity<Map<String, Object>> getReplacementStats() {
        return ResponseEntity.ok(reportsService.getReplacementStats());
    }
}
