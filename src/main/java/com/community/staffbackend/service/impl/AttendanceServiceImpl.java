package com.community.staffbackend.service.impl;

import com.community.staffbackend.dto.request.AttendanceCheckInRequestDto;
import com.community.staffbackend.dto.request.AttendanceCheckOutRequestDto;
import com.community.staffbackend.dto.response.AttendanceResponseDto;
import com.community.staffbackend.entity.Attendance;
import com.community.staffbackend.entity.Staff;
import com.community.staffbackend.exception.ResourceNotFoundException;
import com.community.staffbackend.repository.AttendanceRepository;
import com.community.staffbackend.repository.StaffRepository;
import com.community.staffbackend.service.AttendanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StaffRepository staffRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, StaffRepository staffRepository) {
        this.attendanceRepository = attendanceRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public AttendanceResponseDto checkIn(AttendanceCheckInRequestDto requestDto) {
        Staff staff = staffRepository.findById(requestDto.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + requestDto.getStaffId()));

        LocalDate attendanceDate = requestDto.getDate() != null ? requestDto.getDate() : LocalDate.now();
        LocalDateTime checkInTime = requestDto.getCheckInTime() != null ? requestDto.getCheckInTime() : LocalDateTime.now();

        Attendance attendance = attendanceRepository.findByStaffIdAndDate(staff.getId(), attendanceDate)
                .orElseGet(Attendance::new);

        attendance.setStaff(staff);
        attendance.setDate(attendanceDate);
        attendance.setCheckInTime(checkInTime);
        attendance.setStatus("present");
        attendance.setGate("Gate 1");
        attendance.setShiftStart("09:00");
        attendance.setShiftEnd("18:00");

        staff.setIsInsideCommunity(true);
        staff.setLastCheckIn(checkInTime);
        staffRepository.save(staff);

        Attendance saved = attendanceRepository.save(attendance);
        return mapToAttendanceResponseDto(saved);
    }

    @Override
    public AttendanceResponseDto checkOut(AttendanceCheckOutRequestDto requestDto) {
        Staff staff = staffRepository.findById(requestDto.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + requestDto.getStaffId()));

        LocalDate attendanceDate = requestDto.getDate() != null ? requestDto.getDate() : LocalDate.now();
        LocalDateTime checkOutTime = requestDto.getCheckOutTime() != null ? requestDto.getCheckOutTime() : LocalDateTime.now();

        Attendance attendance = attendanceRepository.findByStaffIdAndDate(staff.getId(), attendanceDate)
                .orElseGet(() -> {
                    Attendance a = new Attendance();
                    a.setStaff(staff);
                    a.setDate(attendanceDate);
                    a.setStatus("present");
                    return a;
                });

        attendance.setCheckOutTime(checkOutTime);
        if (attendance.getCheckInTime() != null) {
            long minutes = java.time.Duration.between(attendance.getCheckInTime(), checkOutTime).toMinutes();
            attendance.setTotalHours(String.format("%.1f", minutes / 60.0));
        }

        staff.setIsInsideCommunity(false);
        staff.setLastCheckOut(checkOutTime);
        staffRepository.save(staff);

        Attendance saved = attendanceRepository.save(attendance);
        return mapToAttendanceResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceResponseDto getAttendanceById(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + id));
        return mapToAttendanceResponseDto(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDto> getAttendanceRecords(LocalDate date, Long staffId, String status) {
        return getAttendanceHistory(staffId != null ? staffId.toString() : null, date, status, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDto> getAttendanceHistory(String staffId, LocalDate date, String status, String search) {
        List<Attendance> records = attendanceRepository.searchAttendance(
                staffId, date, status, (search != null && !search.trim().isEmpty()) ? search.trim() : null);

        List<AttendanceResponseDto> dtos = new ArrayList<>();
        for (Attendance a : records) {
            dtos.add(mapToAttendanceResponseDto(a));
        }
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getEntryExitLogs() {
        List<Attendance> records = attendanceRepository.findAllByOrderByCheckInTimeDesc();
        List<Map<String, Object>> logs = new ArrayList<>();

        for (Attendance a : records) {
            if (a.getCheckInTime() != null) {
                Map<String, Object> inLog = new HashMap<>();
                inLog.put("id", "LOG-IN-" + a.getId());
                inLog.put("staffId", a.getStaff().getStaffId());
                inLog.put("staffName", a.getStaff().getFullName());
                inLog.put("type", "check_in");
                inLog.put("timestamp", a.getCheckInTime().toString());
                inLog.put("gate", a.getGate() != null ? a.getGate() : "Gate 1");
                logs.add(inLog);
            }
            if (a.getCheckOutTime() != null) {
                Map<String, Object> outLog = new HashMap<>();
                outLog.put("id", "LOG-OUT-" + a.getId());
                outLog.put("staffId", a.getStaff().getStaffId());
                outLog.put("staffName", a.getStaff().getFullName());
                outLog.put("type", "check_out");
                outLog.put("timestamp", a.getCheckOutTime().toString());
                outLog.put("gate", a.getGate() != null ? a.getGate() : "Gate 1");
                logs.add(outLog);
            }
        }

        return logs;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getActivityFeed() {
        List<Attendance> records = attendanceRepository.findAllByOrderByCheckInTimeDesc();
        List<Map<String, Object>> feed = new ArrayList<>();

        for (Attendance a : records) {
            if (a.getCheckInTime() != null) {
                Map<String, Object> act = new HashMap<>();
                act.put("id", "ACT-IN-" + a.getId());
                act.put("type", "check_in");
                act.put("staffName", a.getStaff().getFullName());
                act.put("details", "Checked in at " + (a.getGate() != null ? a.getGate() : "Gate 1"));
                act.put("timestamp", a.getCheckInTime().toString());
                feed.add(act);
            }
        }

        return feed;
    }

    private AttendanceResponseDto mapToAttendanceResponseDto(Attendance a) {
        AttendanceResponseDto dto = new AttendanceResponseDto();
        dto.setId(a.getId());
        dto.setAttendanceCode(a.getAttendanceCode() != null ? a.getAttendanceCode() : "ATT-" + a.getId());
        dto.setStaffId(a.getStaff().getId());
        dto.setStaffCode(a.getStaff().getStaffId());
        dto.setStaffName(a.getStaff().getFullName());
        dto.setCategory(a.getStaff().getCategory());
        dto.setTowerAssigned(a.getStaff().getTowerAssigned());
        dto.setDate(a.getDate());
        dto.setCheckInTime(a.getCheckInTime());
        dto.setCheckOutTime(a.getCheckOutTime());
        dto.setTotalHours(a.getTotalHours() != null ? a.getTotalHours() : "0.0");
        dto.setStatus(a.getStatus());
        dto.setShiftStart(a.getShiftStart() != null ? a.getShiftStart() : "09:00");
        dto.setShiftEnd(a.getShiftEnd() != null ? a.getShiftEnd() : "18:00");
        dto.setGate(a.getGate() != null ? a.getGate() : "Gate 1");
        return dto;
    }
}
