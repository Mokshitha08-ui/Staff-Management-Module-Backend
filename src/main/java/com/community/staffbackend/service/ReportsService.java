package com.community.staffbackend.service;

import java.util.List;
import java.util.Map;

public interface ReportsService {
    Map<String, Object> getAttendanceStats(String range);
    List<Map<String, Object>> getStaffByRole();
    List<Map<String, Object>> getStaffByBlock();
    Map<String, Object> getLeaveStats();
    Map<String, Object> getReplacementStats();
}
