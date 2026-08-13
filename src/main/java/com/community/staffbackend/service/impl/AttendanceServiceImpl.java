package com.community.staffbackend.service.impl;

import com.community.staffbackend.dto.request.AttendanceCheckInRequestDto;
import com.community.staffbackend.dto.request.AttendanceCheckOutRequestDto;
import com.community.staffbackend.dto.response.AttendanceResponseDto;
import com.community.staffbackend.entity.Attendance;
import com.community.staffbackend.entity.AttendanceStatus;
import com.community.staffbackend.entity.Staff;
import com.community.staffbackend.exception.InvalidOperationException;
import com.community.staffbackend.exception.ResourceNotFoundException;
import com.community.staffbackend.repository.AttendanceRepository;
import com.community.staffbackend.repository.StaffRepository;
import com.community.staffbackend.service.AttendanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Optional<Attendance> existingAttendance = attendanceRepository.findByStaffIdAndDate(staff.getId(), attendanceDate);
        if (existingAttendance.isPresent() && existingAttendance.get().getCheckIn() != null) {
            throw new InvalidOperationException("Staff member has already checked in on " + attendanceDate);
        }

        Attendance attendance = existingAttendance.orElseGet(Attendance::new);
        attendance.setStaff(staff);
        attendance.setDate(attendanceDate);
        attendance.setCheckIn(checkInTime);
        attendance.setStatus(AttendanceStatus.PRESENT);

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
                .orElseThrow(() -> new InvalidOperationException("Cannot check out without checking in first for " + attendanceDate));

        if (attendance.getCheckIn() == null) {
            throw new InvalidOperationException("Cannot check out without checking in first");
        }

        if (checkOutTime.isBefore(attendance.getCheckIn())) {
            throw new InvalidOperationException("Check-out time (" + checkOutTime + ") cannot be earlier than check-in time (" + attendance.getCheckIn() + ")");
        }

        attendance.setCheckOut(checkOutTime);
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
    public List<AttendanceResponseDto> getAttendanceRecords(LocalDate date, Long staffId, AttendanceStatus status) {
        List<Attendance> records = attendanceRepository.searchAttendance(date, staffId, status);
        return records.stream()
                .map(this::mapToAttendanceResponseDto)
                .collect(Collectors.toList());
    }

    private AttendanceResponseDto mapToAttendanceResponseDto(Attendance attendance) {
        AttendanceResponseDto dto = new AttendanceResponseDto();
        dto.setId(attendance.getId());
        dto.setStaffId(attendance.getStaff().getId());
        dto.setStaffName(attendance.getStaff().getFullName());
        dto.setStaffCode(attendance.getStaff().getStaffId());
        dto.setDate(attendance.getDate());
        dto.setCheckIn(attendance.getCheckIn());
        dto.setCheckOut(attendance.getCheckOut());
        dto.setStatus(attendance.getStatus());
        return dto;
    }
}
