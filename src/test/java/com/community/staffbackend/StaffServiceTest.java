package com.community.staffbackend;

import com.community.staffbackend.dto.request.StaffCreateRequestDto;
import com.community.staffbackend.dto.response.StaffProfileResponseDto;
import com.community.staffbackend.dto.response.StaffResponseDto;
import com.community.staffbackend.exception.DuplicateResourceException;
import com.community.staffbackend.exception.ResourceNotFoundException;
import com.community.staffbackend.service.StaffService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StaffServiceTest {

    @Autowired
    private StaffService staffService;

    @Test
    public void testCreateStaffSuccess() {
        StaffCreateRequestDto dto = new StaffCreateRequestDto();
        dto.setStaffId("STF-TEST-001");
        dto.setFullName("John Doe");
        dto.setPhone("+1234567890");
        dto.setRole("Security Guard");
        dto.setCategory("guard");
        dto.setStatus("active");
        dto.setJoinDate(LocalDateTime.now());

        StaffResponseDto created = staffService.createStaff(dto);

        assertNotNull(created.getId());
        assertEquals("STF-TEST-001", created.getStaffId());
        assertEquals("John Doe", created.getFullName());
        assertEquals("active", created.getStatus());
    }

    @Test
    public void testDuplicateStaffIdThrowsException() {
        StaffCreateRequestDto dto1 = new StaffCreateRequestDto();
        dto1.setStaffId("STF-DUP-001");
        dto1.setFullName("Alice");
        dto1.setPhone("+1111111111");
        dto1.setRole("Cleaner");
        dto1.setCategory("gardener");
        dto1.setStatus("active");

        staffService.createStaff(dto1);

        StaffCreateRequestDto dto2 = new StaffCreateRequestDto();
        dto2.setStaffId("STF-DUP-001");
        dto2.setFullName("Bob");
        dto2.setPhone("+2222222222");
        dto2.setRole("Gardener");
        dto2.setCategory("gardener");
        dto2.setStatus("active");

        assertThrows(DuplicateResourceException.class, () -> {
            staffService.createStaff(dto2);
        });
    }

    @Test
    public void testGetStaffProfileSuccess() {
        StaffCreateRequestDto dto = new StaffCreateRequestDto();
        dto.setStaffId("STF-PROF-001");
        dto.setFullName("Sarah Connor");
        dto.setPhone("+3333333333");
        dto.setRole("Supervisor");
        dto.setCategory("guard");
        dto.setTowerAssigned("Tower A");
        dto.setStatus("active");

        StaffResponseDto created = staffService.createStaff(dto);

        StaffProfileResponseDto profile = staffService.getStaffProfile(created.getId());

        assertEquals(created.getId(), profile.getId());
        assertEquals("Sarah Connor", profile.getFullName());
        assertEquals("Tower A", profile.getTodaysAssignedArea());
    }

    @Test
    public void testGetStaffNotFoundThrowsException() {
        assertThrows(ResourceNotFoundException.class, () -> {
            staffService.getStaffById(99999L);
        });
    }
}
