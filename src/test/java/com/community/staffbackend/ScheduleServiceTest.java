package com.community.staffbackend;

import com.community.staffbackend.dto.request.ScheduleCreateRequestDto;
import com.community.staffbackend.dto.request.StaffCreateRequestDto;
import com.community.staffbackend.dto.response.ScheduleResponseDto;
import com.community.staffbackend.dto.response.StaffResponseDto;
import com.community.staffbackend.entity.StaffStatus;
import com.community.staffbackend.service.ScheduleService;
import com.community.staffbackend.service.StaffService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ScheduleServiceTest {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private StaffService staffService;

    @Test
    public void testCreateScheduleSuccess() {
        StaffCreateRequestDto staffDto = new StaffCreateRequestDto();
        staffDto.setStaffId("STF-SCH-001");
        staffDto.setFullName("Ravi Kumar");
        staffDto.setPhone("+919876543210");
        staffDto.setRole("Security Guard");
        staffDto.setStatus(StaffStatus.ACTIVE);
        StaffResponseDto staff = staffService.createStaff(staffDto);

        ScheduleCreateRequestDto req = new ScheduleCreateRequestDto();
        req.setStaffId(staff.getId());
        req.setDate(LocalDate.now());
        req.setShiftStart(LocalTime.of(8, 0));
        req.setShiftEnd(LocalTime.of(16, 0));
        req.setAssignedArea("Block A");
        req.setAssignedTask("Gate Patrol");

        ScheduleResponseDto schedule = scheduleService.createSchedule(req);

        assertNotNull(schedule.getId());
        assertEquals("Block A", schedule.getAssignedArea());
        assertEquals(staff.getId(), schedule.getStaffId());

        List<ScheduleResponseDto> list = scheduleService.getSchedules(LocalDate.now(), staff.getId(), "Block A");
        assertFalse(list.isEmpty());
    }
}
