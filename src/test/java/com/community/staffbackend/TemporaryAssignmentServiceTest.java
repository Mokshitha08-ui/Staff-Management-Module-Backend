package com.community.staffbackend;

import com.community.staffbackend.dto.request.StaffCreateRequestDto;
import com.community.staffbackend.dto.request.TemporaryAssignmentCreateRequestDto;
import com.community.staffbackend.dto.response.StaffResponseDto;
import com.community.staffbackend.dto.response.TemporaryAssignmentResponseDto;
import com.community.staffbackend.service.StaffService;
import com.community.staffbackend.service.TemporaryAssignmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TemporaryAssignmentServiceTest {

    @Autowired
    private TemporaryAssignmentService tempService;

    @Autowired
    private StaffService staffService;

    private StaffResponseDto createStaff(String staffId, String name) {
        StaffCreateRequestDto dto = new StaffCreateRequestDto();
        dto.setStaffId(staffId);
        dto.setFullName(name);
        dto.setPhone("+12345678");
        dto.setRole("Maintenance");
        dto.setCategory("electrician");
        dto.setStatus("active");
        return staffService.createStaff(dto);
    }

    @Test
    public void testCreateAssignmentSuccess() {
        StaffResponseDto regular = createStaff("STF-TMP-001", "Regular Worker");
        StaffResponseDto replacement = createStaff("STF-TMP-002", "Replacement Worker");

        TemporaryAssignmentCreateRequestDto req = new TemporaryAssignmentCreateRequestDto();
        req.setRegularStaffId(regular.getId());
        req.setReplacementStaffId(replacement.getId());
        req.setStartDate(LocalDate.now());
        req.setEndDate(LocalDate.now().plusDays(5));
        req.setAssignedArea("Clubhouse");
        req.setReason("Sick leave");

        TemporaryAssignmentResponseDto response = tempService.createAssignment(req);

        assertNotNull(response.getId());
        assertEquals(regular.getId(), response.getRegularStaffId());
        assertEquals(replacement.getId(), response.getReplacementStaffId());
    }
}
