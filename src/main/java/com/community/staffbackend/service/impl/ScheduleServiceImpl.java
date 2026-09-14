package com.community.staffbackend.service.impl;

import com.community.staffbackend.dto.request.ScheduleCreateRequestDto;
import com.community.staffbackend.dto.request.ScheduleUpdateRequestDto;
import com.community.staffbackend.dto.response.ScheduleResponseDto;
import com.community.staffbackend.dto.response.StaffResponseDto;
import com.community.staffbackend.entity.Schedule;
import com.community.staffbackend.entity.Staff;
import com.community.staffbackend.exception.ResourceNotFoundException;
import com.community.staffbackend.repository.ScheduleRepository;
import com.community.staffbackend.repository.StaffRepository;
import com.community.staffbackend.service.ScheduleService;
import com.community.staffbackend.service.StaffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final StaffRepository staffRepository;
    private final StaffService staffService;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, StaffRepository staffRepository, StaffService staffService) {
        this.scheduleRepository = scheduleRepository;
        this.staffRepository = staffRepository;
        this.staffService = staffService;
    }

    @Override
    public ScheduleResponseDto createSchedule(ScheduleCreateRequestDto requestDto) {
        Staff staff = staffRepository.findById(requestDto.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + requestDto.getStaffId()));

        Schedule schedule = new Schedule();
        schedule.setStaff(staff);
        schedule.setDate(requestDto.getDate() != null ? requestDto.getDate() : LocalDate.now());
        schedule.setShift(requestDto.getShift() != null ? requestDto.getShift() : "morning");
        schedule.setStartTime(requestDto.getShiftStart() != null ? requestDto.getShiftStart().toString() : "08:00");
        schedule.setEndTime(requestDto.getShiftEnd() != null ? requestDto.getShiftEnd().toString() : "17:00");
        schedule.setTowerAssigned(requestDto.getAssignedArea() != null ? requestDto.getAssignedArea() : staff.getTowerAssigned());
        schedule.setBlockAssigned(staff.getBlockAssigned());

        Schedule saved = scheduleRepository.save(schedule);
        return mapToScheduleResponseDto(saved);
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
    public List<ScheduleResponseDto> getSchedules(LocalDate date, String staffId, String shift) {
        List<Schedule> schedules = scheduleRepository.searchSchedules(staffId, date, shift);
        return schedules.stream().map(this::mapToScheduleResponseDto).collect(Collectors.toList());
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, ScheduleUpdateRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + id));

        if (requestDto.getDate() != null) schedule.setDate(requestDto.getDate());
        if (requestDto.getShiftStart() != null) schedule.setStartTime(requestDto.getShiftStart().toString());
        if (requestDto.getShiftEnd() != null) schedule.setEndTime(requestDto.getShiftEnd().toString());
        if (requestDto.getAssignedArea() != null) schedule.setTowerAssigned(requestDto.getAssignedArea());

        Schedule updated = scheduleRepository.save(schedule);
        return mapToScheduleResponseDto(updated);
    }

    @Override
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Schedule not found with ID: " + id);
        }
        scheduleRepository.deleteById(id);
    }

    @Override
    public ScheduleResponseDto assignShift(String staffId, Map<String, Object> shiftData) {
        Staff staff = findStaff(staffId);

        LocalDate date = shiftData.get("date") != null ? LocalDate.parse(shiftData.get("date").toString()) : LocalDate.now();
        Optional<Schedule> existing = scheduleRepository.findByStaffIdAndDate(staff.getId(), date);

        Schedule schedule = existing.orElseGet(Schedule::new);
        schedule.setStaff(staff);
        schedule.setDate(date);
        schedule.setShift(shiftData.get("shift") != null ? shiftData.get("shift").toString() : "morning");
        schedule.setStartTime(shiftData.get("startTime") != null ? shiftData.get("startTime").toString() : "08:00");
        schedule.setEndTime(shiftData.get("endTime") != null ? shiftData.get("endTime").toString() : "17:00");

        if (shiftData.get("towerAssigned") != null) {
            schedule.setTowerAssigned(shiftData.get("towerAssigned").toString());
            staff.setTowerAssigned(shiftData.get("towerAssigned").toString());
        }
        if (shiftData.get("blockAssigned") != null) {
            schedule.setBlockAssigned(shiftData.get("blockAssigned").toString());
            staff.setBlockAssigned(shiftData.get("blockAssigned").toString());
        }

        staffRepository.save(staff);
        Schedule saved = scheduleRepository.save(schedule);
        return mapToScheduleResponseDto(saved);
    }

    @Override
    public StaffResponseDto assignArea(String staffId, Object areaData) {
        Staff staff = findStaff(staffId);

        String tower = areaData instanceof Map ? ((Map<?, ?>) areaData).get("area").toString() : areaData.toString();
        staff.setTowerAssigned(tower);
        Staff saved = staffRepository.save(staff);

        return staffService.getStaffById(saved.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getLeaveSchedule() {
        List<Staff> leaveStaff = staffRepository.findByStatus("on_leave");
        List<Map<String, Object>> list = new ArrayList<>();

        for (Staff s : leaveStaff) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", "LVE-" + s.getId());
            item.put("staffId", s.getStaffId());
            item.put("staffName", s.getFullName());
            item.put("category", s.getCategory());
            item.put("towerAssigned", s.getTowerAssigned() != null ? s.getTowerAssigned() : "Tower A");
            item.put("startDate", s.getLeaveStartDate() != null ? s.getLeaveStartDate().toLocalDate().toString() : LocalDate.now().toString());
            item.put("endDate", s.getLeaveEndDate() != null ? s.getLeaveEndDate().toLocalDate().toString() : LocalDate.now().plusDays(7).toString());
            item.put("reason", "Approved Leave");
            item.put("status", "approved");
            list.add(item);
        }

        return list;
    }

    private Staff findStaff(String staffId) {
        try {
            Long id = Long.parseLong(staffId);
            return staffRepository.findById(id).orElseGet(() -> staffRepository.findByStaffId(staffId)
                    .orElseThrow(() -> new ResourceNotFoundException("Staff member not found: " + staffId)));
        } catch (NumberFormatException e) {
            return staffRepository.findByStaffId(staffId)
                    .orElseThrow(() -> new ResourceNotFoundException("Staff member not found: " + staffId));
        }
    }

    private ScheduleResponseDto mapToScheduleResponseDto(Schedule s) {
        ScheduleResponseDto dto = new ScheduleResponseDto();
        dto.setId(s.getId());
        dto.setScheduleCode(s.getScheduleCode() != null ? s.getScheduleCode() : "SCH-" + s.getId());
        dto.setStaffId(s.getStaff().getId());
        dto.setStaffCode(s.getStaff().getStaffId());
        dto.setStaffName(s.getStaff().getFullName());
        dto.setCategory(s.getStaff().getCategory());
        dto.setTowerAssigned(s.getTowerAssigned() != null ? s.getTowerAssigned() : s.getStaff().getTowerAssigned());
        dto.setBlockAssigned(s.getBlockAssigned() != null ? s.getBlockAssigned() : s.getStaff().getBlockAssigned());
        dto.setShift(s.getShift() != null ? s.getShift() : "morning");
        dto.setStartTime(s.getStartTime() != null ? s.getStartTime() : "08:00");
        dto.setEndTime(s.getEndTime() != null ? s.getEndTime() : "17:00");
        dto.setDate(s.getDate());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setUpdatedAt(s.getUpdatedAt());
        return dto;
    }
}
