package com.community.staffbackend.service;

import com.community.staffbackend.dto.request.AttendanceCheckInRequestDto;
import com.community.staffbackend.dto.request.AttendanceCheckOutRequestDto;
import com.community.staffbackend.dto.response.AttendanceResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AttendanceService {
    AttendanceResponseDto checkIn(AttendanceCheckInRequestDto requestDto);
    AttendanceResponseDto checkOut(AttendanceCheckOutRequestDto requestDto);
    AttendanceResponseDto getAttendanceById(Long id);
    List<AttendanceResponseDto> getAttendanceRecords(LocalDate date, Long staffId, String status);
    List<AttendanceResponseDto> getAttendanceHistory(String staffId, LocalDate date, String status, String search);
    List<Map<String, Object>> getEntryExitLogs();
    List<Map<String, Object>> getActivityFeed();
}
