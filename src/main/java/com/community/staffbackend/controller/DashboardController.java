package com.community.staffbackend.controller;

import com.community.staffbackend.dto.response.DashboardResponseDto;
import com.community.staffbackend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponseDto> getDashboardStatistics() {
        DashboardResponseDto dashboard = dashboardService.getDashboardStatistics();
        return ResponseEntity.ok(dashboard);
    }
}
