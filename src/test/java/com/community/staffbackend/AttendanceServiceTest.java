package com.community.staffbackend;

import com.community.staffbackend.dto.request.AttendanceCheckInRequestDto;
import com.community.staffbackend.dto.request.AttendanceCheckOutRequestDto;
import com.community.staffbackend.dto.request.StaffCreateRequestDto;
import com.community.staffbackend.dto.response.AttendanceResponseDto;
import com.community.staffbackend.dto.response.StaffResponseDto;
import com.community.staffbackend.service.AttendanceService;
import com.community.staffbackend.service.StaffService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AttendanceServiceTest {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private StaffService staffService;

    private StaffResponseDto createTestStaff(String staffId, String name) {
        StaffCreateRequestDto dto = new StaffCreateRequestDto();
        dto.setStaffId(staffId);
        dto.setFullName(name);
        dto.setPhone("+1234567");
        dto.setRole("Technician");
        dto.setCategory("electrician");
        dto.setStatus("active");
        return staffService.createStaff(dto);
    }

    @Test
    public void testCheckInAndCheckOutSuccess() {
        StaffResponseDto staff = createTestStaff("STF-ATT-001", "Mark Taylor");

        AttendanceCheckInRequestDto inDto = new AttendanceCheckInRequestDto();
        inDto.setStaffId(staff.getId());
        inDto.setDate(LocalDate.now());
        inDto.setCheckInTime(LocalDateTime.now().minusHours(8));

        AttendanceResponseDto inResponse = attendanceService.checkIn(inDto);
        assertNotNull(inResponse.getId());
        assertEquals("present", inResponse.getStatus());
        assertNotNull(inResponse.getCheckInTime());
        assertNull(inResponse.getCheckOutTime());

        AttendanceCheckOutRequestDto outDto = new AttendanceCheckOutRequestDto();
        outDto.setStaffId(staff.getId());
        outDto.setDate(LocalDate.now());
        outDto.setCheckOutTime(LocalDateTime.now());

        AttendanceResponseDto outResponse = attendanceService.checkOut(outDto);
        assertNotNull(outResponse.getCheckOutTime());
    }
}
