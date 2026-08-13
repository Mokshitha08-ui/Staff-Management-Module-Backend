package com.community.staffbackend.service;

import com.community.staffbackend.dto.request.AttendanceCheckInRequestDto;
import com.community.staffbackend.dto.request.AttendanceCheckOutRequestDto;
import com.community.staffbackend.dto.response.AttendanceResponseDto;
import com.community.staffbackend.entity.AttendanceStatus;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    AttendanceResponseDto checkIn(AttendanceCheckInRequestDto requestDto);
    AttendanceResponseDto checkOut(AttendanceCheckOutRequestDto requestDto);
    AttendanceResponseDto getAttendanceById(Long id);
    List<AttendanceResponseDto> getAttendanceRecords(LocalDate date, Long staffId, AttendanceStatus status);
}
