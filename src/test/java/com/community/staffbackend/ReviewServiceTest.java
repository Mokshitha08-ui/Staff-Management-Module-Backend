package com.community.staffbackend;

import com.community.staffbackend.dto.request.ReviewCreateRequestDto;
import com.community.staffbackend.dto.request.StaffCreateRequestDto;
import com.community.staffbackend.dto.response.ReviewResponseDto;
import com.community.staffbackend.dto.response.StaffResponseDto;
import com.community.staffbackend.entity.StaffStatus;
import com.community.staffbackend.exception.InvalidOperationException;
import com.community.staffbackend.service.ReviewService;
import com.community.staffbackend.service.StaffService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private StaffService staffService;

    @Test
    public void testCreateReviewSuccess() {
        StaffCreateRequestDto staffDto = new StaffCreateRequestDto();
        staffDto.setStaffId("STF-REV-001");
        staffDto.setFullName("Gardener Sam");
        staffDto.setPhone("+12345");
        staffDto.setRole("Gardener");
        staffDto.setStatus(StaffStatus.ACTIVE);
        StaffResponseDto staff = staffService.createStaff(staffDto);

        ReviewCreateRequestDto req = new ReviewCreateRequestDto();
        req.setStaffId(staff.getId());
        req.setRating(5);
        req.setReviewText("Excellent garden maintenance!");
        req.setReviewerName("Resident 101");

        ReviewResponseDto review = reviewService.createReview(req);

        assertNotNull(review.getId());
        assertEquals(5, review.getRating());
        assertEquals("Resident 101", review.getReviewerName());
    }

    @Test
    public void testInvalidRatingThrowsException() {
        StaffCreateRequestDto staffDto = new StaffCreateRequestDto();
        staffDto.setStaffId("STF-REV-002");
        staffDto.setFullName("Cleaner Jane");
        staffDto.setPhone("+54321");
        staffDto.setRole("Cleaner");
        staffDto.setStatus(StaffStatus.ACTIVE);
        StaffResponseDto staff = staffService.createStaff(staffDto);

        ReviewCreateRequestDto req = new ReviewCreateRequestDto();
        req.setStaffId(staff.getId());
        req.setRating(6); // Invalid > 5

        assertThrows(Exception.class, () -> {
            reviewService.createReview(req);
        });
    }
}
