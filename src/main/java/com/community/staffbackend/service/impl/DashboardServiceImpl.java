package com.community.staffbackend.service.impl;

import com.community.staffbackend.dto.response.DashboardResponseDto;
import com.community.staffbackend.repository.StaffRepository;
import com.community.staffbackend.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final StaffRepository staffRepository;

    public DashboardServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public DashboardResponseDto getDashboardStatistics() {
        long totalStaff = staffRepository.count();
        long activeStaff = staffRepository.countByStatus("active");
        long inactiveStaff = staffRepository.countByStatus("inactive") + staffRepository.countByStatus("suspended");
        long currentlyInside = staffRepository.countByIsInsideCommunityTrue();
        long pendingVerification = staffRepository.countByStatus("pending") + staffRepository.countByStatus("under_review");
        long onLeave = staffRepository.countByStatus("on_leave");

        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        long newThisMonth = staffRepository.countByJoinDateAfter(monthStart);

        LocalDateTime ninetyDaysFuture = LocalDateTime.now().plusDays(90);
        long expiringDocuments = staffRepository.countExpiringDocuments(ninetyDaysFuture);

        return new DashboardResponseDto(
                totalStaff,
                activeStaff,
                inactiveStaff,
                currentlyInside,
                pendingVerification,
                onLeave,
                newThisMonth,
                expiringDocuments
        );
    }
}
