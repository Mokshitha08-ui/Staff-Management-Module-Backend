package com.community.staffbackend.service;

import com.community.staffbackend.dto.request.ScheduleCreateRequestDto;
import com.community.staffbackend.dto.request.ScheduleUpdateRequestDto;
import com.community.staffbackend.dto.response.ScheduleResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto createSchedule(ScheduleCreateRequestDto requestDto);
    ScheduleResponseDto getScheduleById(Long id);
    List<ScheduleResponseDto> getSchedules(LocalDate date, Long staffId, String assignedArea);
    ScheduleResponseDto updateSchedule(Long id, ScheduleUpdateRequestDto requestDto);
    void deleteSchedule(Long id);
}
