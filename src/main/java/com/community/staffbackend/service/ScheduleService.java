package com.community.staffbackend.service;

import com.community.staffbackend.dto.request.ScheduleCreateRequestDto;
import com.community.staffbackend.dto.request.ScheduleUpdateRequestDto;
import com.community.staffbackend.dto.response.ScheduleResponseDto;
import com.community.staffbackend.dto.response.StaffResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ScheduleService {
    ScheduleResponseDto createSchedule(ScheduleCreateRequestDto requestDto);
    ScheduleResponseDto getScheduleById(Long id);
    List<ScheduleResponseDto> getSchedules(LocalDate date, String staffId, String shift);
    ScheduleResponseDto updateSchedule(Long id, ScheduleUpdateRequestDto requestDto);
    void deleteSchedule(Long id);

    ScheduleResponseDto assignShift(String staffId, Map<String, Object> shiftData);
    StaffResponseDto assignArea(String staffId, Object areaData);
    List<Map<String, Object>> getLeaveSchedule();
}
