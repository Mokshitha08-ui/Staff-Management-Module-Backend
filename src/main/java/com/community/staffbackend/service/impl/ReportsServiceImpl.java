package com.community.staffbackend.service.impl;

import com.community.staffbackend.entity.Attendance;
import com.community.staffbackend.entity.Staff;
import com.community.staffbackend.entity.TemporaryAssignment;
import com.community.staffbackend.repository.AttendanceRepository;
import com.community.staffbackend.repository.StaffRepository;
import com.community.staffbackend.repository.TemporaryAssignmentRepository;
import com.community.staffbackend.service.ReportsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class ReportsServiceImpl implements ReportsService {

    private final StaffRepository staffRepository;
    private final TemporaryAssignmentRepository assignmentRepository;
    private final AttendanceRepository attendanceRepository;

    private static final Map<String, String> CATEGORY_LABELS = Map.of(
            "guard", "Guard",
            "driver", "Driver",
            "gardener", "Gardener",
            "electrician", "Electrician",
            "plumber", "Plumber",
            "cook", "Cook",
            "nanny", "Nanny",
            "other", "Other"
    );

    public ReportsServiceImpl(StaffRepository staffRepository,
                              TemporaryAssignmentRepository assignmentRepository,
                              AttendanceRepository attendanceRepository) {
        this.staffRepository = staffRepository;
        this.assignmentRepository = assignmentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public Map<String, Object> getAttendanceStats(String range) {
        List<Attendance> allAttendance = attendanceRepository.findAll();
        Map<String, Object> response = new HashMap<>();

        if (allAttendance.isEmpty()) {
            response.put("avgRate", "0%");
            response.put("totalLate", 0);
            response.put("totalEarlyLeave", 0);
            response.put("trend", Collections.emptyList());
            return response;
        }

        long presentCount = allAttendance.stream().filter(a -> "present".equalsIgnoreCase(a.getStatus()) || "on_duty".equalsIgnoreCase(a.getStatus())).count();
        long lateCount = allAttendance.stream().filter(a -> "late".equalsIgnoreCase(a.getStatus())).count();
        long earlyLeaveCount = allAttendance.stream().filter(a -> "early_leave".equalsIgnoreCase(a.getStatus())).count();
        double avgRate = (allAttendance.size() > 0) ? (double) presentCount / allAttendance.size() * 100 : 0.0;

        response.put("avgRate", String.format(Locale.US, "%.1f%%", avgRate));
        response.put("totalLate", (int) lateCount);
        response.put("totalEarlyLeave", (int) earlyLeaveCount);
        response.put("trend", Collections.emptyList());
        return response;
    }

    @Override
    public List<Map<String, Object>> getStaffByRole() {
        List<Staff> staffList = staffRepository.findAll();
        Map<String, Integer> countMap = new HashMap<>();

        for (Staff s : staffList) {
            String cat = s.getCategory() != null ? s.getCategory().toLowerCase() : "other";
            countMap.put(cat, countMap.getOrDefault(cat, 0) + 1);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        countMap.forEach((cat, count) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", CATEGORY_LABELS.getOrDefault(cat, cat.length() > 0 ? cat.substring(0, 1).toUpperCase() + cat.substring(1) : "Other"));
            item.put("count", count);
            result.add(item);
        });

        return result;
    }

    @Override
    public List<Map<String, Object>> getStaffByBlock() {
        List<Staff> staffList = staffRepository.findAll();
        Map<String, Integer> blockMap = new HashMap<>();

        for (Staff s : staffList) {
            String block = s.getTowerAssigned() != null ? s.getTowerAssigned() : "Unassigned";
            blockMap.put(block, blockMap.getOrDefault(block, 0) + 1);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        blockMap.forEach((block, count) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("block", block);
            item.put("count", count);
            result.add(item);
        });

        return result;
    }

    @Override
    public Map<String, Object> getLeaveStats() {
        long activeOnLeave = staffRepository.countByStatus("on_leave");
        Map<String, Object> response = new HashMap<>();
        response.put("activeOnLeave", activeOnLeave);
        response.put("avgLeaveDays", activeOnLeave > 0 ? "2.5 days" : "0 days");
        response.put("trend", Collections.emptyList());
        return response;
    }

    @Override
    public Map<String, Object> getReplacementStats() {
        long activeCount = assignmentRepository.countByStatus("active");
        List<TemporaryAssignment> assignments = assignmentRepository.findAll();
        Map<String, Integer> reasonsMap = new HashMap<>();

        for (TemporaryAssignment a : assignments) {
            String reason = a.getReason() != null ? a.getReason() : "Coverage";
            reasonsMap.put(reason, reasonsMap.getOrDefault(reason, 0) + 1);
        }

        List<Map<String, Object>> reasonsBreakdown = new ArrayList<>();
        reasonsMap.forEach((reason, count) -> {
            reasonsBreakdown.add(Map.of("reason", reason, "count", count));
        });

        Map<String, Object> response = new HashMap<>();
        response.put("activeReplacements", activeCount);
        response.put("avgDuration", assignments.size() > 0 ? "5 days" : "0 days");
        response.put("reasonsBreakdown", assignments.isEmpty() ? Collections.emptyList() : reasonsBreakdown);
        return response;
    }
}
