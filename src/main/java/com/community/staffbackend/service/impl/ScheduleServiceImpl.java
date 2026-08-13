package com.community.staffbackend.service.impl;

import com.community.staffbackend.dto.request.ScheduleCreateRequestDto;
import com.community.staffbackend.dto.request.ScheduleUpdateRequestDto;
import com.community.staffbackend.dto.response.ScheduleResponseDto;
import com.community.staffbackend.entity.Schedule;
import com.community.staffbackend.entity.Staff;
import com.community.staffbackend.exception.ResourceNotFoundException;
import com.community.staffbackend.repository.ScheduleRepository;
import com.community.staffbackend.repository.StaffRepository;
import com.community.staffbackend.service.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final StaffRepository staffRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, StaffRepository staffRepository) {
        this.scheduleRepository = scheduleRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public ScheduleResponseDto createSchedule(ScheduleCreateRequestDto requestDto) {
        Staff staff = staffRepository.findById(requestDto.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + requestDto.getStaffId()));

        Schedule schedule = new Schedule();
        schedule.setStaff(staff);
        schedule.setDate(requestDto.getDate());
        schedule.setShiftStart(requestDto.getShiftStart());
        schedule.setShiftEnd(requestDto.getShiftEnd());
        schedule.setAssignedArea(requestDto.getAssignedArea());
        schedule.setAssignedTask(requestDto.getAssignedTask());

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return mapToScheduleResponseDto(savedSchedule);
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResponseDto getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + id));
        return mapToScheduleResponseDto(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getSchedules(LocalDate date, Long staffId, String assignedArea) {
        List<Schedule> schedules = scheduleRepository.searchSchedules(date, staffId,
                (assignedArea != null && !assignedArea.trim().isEmpty()) ? assignedArea.trim() : null);

        return schedules.stream()
                .map(this::mapToScheduleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleUpdateRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + id));

        if (requestDto.getDate() != null) schedule.setDate(requestDto.getDate());
        if (requestDto.getShiftStart() != null) schedule.setShiftStart(requestDto.getShiftStart());
        if (requestDto.getShiftEnd() != null) schedule.setShiftEnd(requestDto.getShiftEnd());
        if (requestDto.getAssignedArea() != null) schedule.setAssignedArea(requestDto.getAssignedArea());
        if (requestDto.getAssignedTask() != null) schedule.setAssignedTask(requestDto.getAssignedTask());

        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return mapToScheduleResponseDto(updatedSchedule);
    }

    @Override
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Schedule not found with ID: " + id);
        }
        scheduleRepository.deleteById(id);
    }

    private ScheduleResponseDto mapToScheduleResponseDto(Schedule schedule) {
        ScheduleResponseDto dto = new ScheduleResponseDto();
        dto.setId(schedule.getId());
        dto.setStaffId(schedule.getStaff().getId());
        dto.setStaffName(schedule.getStaff().getFullName());
        dto.setStaffCode(schedule.getStaff().getStaffId());
        dto.setDate(schedule.getDate());
        dto.setShiftStart(schedule.getShiftStart());
        dto.setShiftEnd(schedule.getShiftEnd());
        dto.setAssignedArea(schedule.getAssignedArea());
        dto.setAssignedTask(schedule.getAssignedTask());
        dto.setCreatedAt(schedule.getCreatedAt());
        dto.setUpdatedAt(schedule.getUpdatedAt());
        return dto;
    }
}
