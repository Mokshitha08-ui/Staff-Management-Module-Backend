package com.community.staffbackend.service.impl;

import com.community.staffbackend.dto.response.DashboardResponseDto;
import com.community.staffbackend.entity.ComplaintStatus;
import com.community.staffbackend.entity.ReplacementStatus;
import com.community.staffbackend.entity.StaffStatus;
import com.community.staffbackend.repository.*;
import com.community.staffbackend.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final StaffRepository staffRepository;
    private final AttendanceRepository attendanceRepository;
    private final TemporaryAssignmentRepository temporaryAssignmentRepository;
    private final ComplaintRepository complaintRepository;
    private final ReviewRepository reviewRepository;

    public DashboardServiceImpl(StaffRepository staffRepository,
                                AttendanceRepository attendanceRepository,
                                TemporaryAssignmentRepository temporaryAssignmentRepository,
                                ComplaintRepository complaintRepository,
                                ReviewRepository reviewRepository) {
        this.staffRepository = staffRepository;
        this.attendanceRepository = attendanceRepository;
        this.temporaryAssignmentRepository = temporaryAssignmentRepository;
        this.complaintRepository = complaintRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public DashboardResponseDto getDashboardStatistics() {
        long totalStaff = staffRepository.count();
        long activeStaff = staffRepository.countByStatus(StaffStatus.ACTIVE);
        long inactiveStaff = staffRepository.countByStatus(StaffStatus.INACTIVE);
        long staffOnLeave = staffRepository.countByStatus(StaffStatus.ON_LEAVE);

        LocalDate today = LocalDate.now();
        long currentlyOnDuty = attendanceRepository.countByDateAndCheckInIsNotNullAndCheckOutIsNull(today);
        long currentlyOffDuty = Math.max(0, totalStaff - currentlyOnDuty - staffOnLeave);

        long temporaryWorkers = temporaryAssignmentRepository.countByStatus(ReplacementStatus.ACTIVE);
        long pendingComplaints = complaintRepository.countByStatus(ComplaintStatus.PENDING);

        Double avgRating = reviewRepository.findGlobalAverageRating();
        double formattedAvgRating = avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0;

        return new DashboardResponseDto(
                totalStaff,
                activeStaff,
                inactiveStaff,
                currentlyOnDuty,
                currentlyOffDuty,
                staffOnLeave,
                temporaryWorkers,
                pendingComplaints,
                formattedAvgRating
        );
    }
}
