package com.community.staffbackend.service.impl;

import com.community.staffbackend.dto.request.StaffCreateRequestDto;
import com.community.staffbackend.dto.request.StaffStatusUpdateRequestDto;
import com.community.staffbackend.dto.request.StaffUpdateRequestDto;
import com.community.staffbackend.dto.response.StaffDirectoryResponseDto;
import com.community.staffbackend.dto.response.StaffProfileResponseDto;
import com.community.staffbackend.dto.response.StaffResponseDto;
import com.community.staffbackend.entity.*;
import com.community.staffbackend.exception.DuplicateResourceException;
import com.community.staffbackend.exception.ResourceNotFoundException;
import com.community.staffbackend.repository.AttendanceRepository;
import com.community.staffbackend.repository.ReviewRepository;
import com.community.staffbackend.repository.ScheduleRepository;
import com.community.staffbackend.repository.StaffRepository;
import com.community.staffbackend.service.StaffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final ScheduleRepository scheduleRepository;
    private final AttendanceRepository attendanceRepository;
    private final ReviewRepository reviewRepository;

    public StaffServiceImpl(StaffRepository staffRepository,
                            ScheduleRepository scheduleRepository,
                            AttendanceRepository attendanceRepository,
                            ReviewRepository reviewRepository) {
        this.staffRepository = staffRepository;
        this.scheduleRepository = scheduleRepository;
        this.attendanceRepository = attendanceRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public StaffResponseDto createStaff(StaffCreateRequestDto requestDto) {
        if (staffRepository.existsByStaffId(requestDto.getStaffId())) {
            throw new DuplicateResourceException("Staff member with ID '" + requestDto.getStaffId() + "' already exists");
        }

        Staff staff = new Staff();
        staff.setStaffId(requestDto.getStaffId());
        staff.setFullName(requestDto.getFullName());
        staff.setPhoto(requestDto.getPhoto());
        staff.setPhone(requestDto.getPhone());
        staff.setAddress(requestDto.getAddress());
        staff.setRole(requestDto.getRole());
        staff.setSkills(requestDto.getSkills());
        staff.setExperience(requestDto.getExperience());
        staff.setJoiningDate(requestDto.getJoiningDate());
        staff.setWorkingHours(requestDto.getWorkingHours());
        staff.setAvailability(requestDto.getAvailability());
        staff.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : StaffStatus.ACTIVE);

        Staff savedStaff = staffRepository.save(staff);
        return mapToStaffResponseDto(savedStaff);
    }

    @Override
    @Transactional(readOnly = true)
    public StaffResponseDto getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));
        return mapToStaffResponseDto(staff);
    }

    @Override
    @Transactional(readOnly = true)
    public StaffProfileResponseDto getStaffProfile(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        LocalDate today = LocalDate.now();

        // 1. Get Today's assigned area from Schedule (Do NOT store in Staff!)
        Optional<Schedule> todaySchedule = scheduleRepository.findByStaffIdAndDate(id, today);
        String todaysArea = todaySchedule.map(Schedule::getAssignedArea).orElse("Unassigned");

        // 2. Calculate Current Duty Status dynamically
        DutyStatus dutyStatus;
        if (staff.getStatus() == StaffStatus.ON_LEAVE) {
            dutyStatus = DutyStatus.ON_LEAVE;
        } else if (staff.getStatus() == StaffStatus.UNAVAILABLE || staff.getStatus() == StaffStatus.SUSPENDED || staff.getStatus() == StaffStatus.INACTIVE) {
            dutyStatus = DutyStatus.UNAVAILABLE;
        } else {
            Optional<Attendance> todayAttendance = attendanceRepository.findByStaffIdAndDate(id, today);
            if (todayAttendance.isPresent() && todayAttendance.get().getCheckIn() != null && todayAttendance.get().getCheckOut() == null) {
                dutyStatus = DutyStatus.ON_DUTY;
            } else {
                dutyStatus = DutyStatus.OFF_DUTY;
            }
        }

        // 3. Aggregate Rating Metrics
        Double avgRating = reviewRepository.findAverageRatingByStaffId(id);
        long reviewCount = reviewRepository.countByStaffId(id);

        StaffProfileResponseDto profile = new StaffProfileResponseDto();
        profile.setId(staff.getId());
        profile.setStaffId(staff.getStaffId());
        profile.setFullName(staff.getFullName());
        profile.setPhoto(staff.getPhoto());
        profile.setPhone(staff.getPhone());
        profile.setAddress(staff.getAddress());
        profile.setRole(staff.getRole());
        profile.setSkills(staff.getSkills());
        profile.setExperience(staff.getExperience());
        profile.setWorkingHours(staff.getWorkingHours());
        profile.setAvailability(staff.getAvailability());
        profile.setStatus(staff.getStatus());
        profile.setCurrentDutyStatus(dutyStatus);
        profile.setTodaysAssignedArea(todaysArea);
        profile.setAverageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
        profile.setReviewCount(reviewCount);

        return profile;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffDirectoryResponseDto> getStaffDirectory(String query, String role, StaffStatus status) {
        List<Staff> staffList = staffRepository.searchAndFilterStaff(
                (query != null && !query.trim().isEmpty()) ? query.trim() : null,
                (role != null && !role.trim().isEmpty()) ? role.trim() : null,
                status
        );

        return staffList.stream().map(staff -> {
            StaffDirectoryResponseDto dto = new StaffDirectoryResponseDto();
            dto.setId(staff.getId());
            dto.setStaffId(staff.getStaffId());
            dto.setFullName(staff.getFullName());
            dto.setPhoto(staff.getPhoto());
            dto.setRole(staff.getRole());
            dto.setSkills(staff.getSkills());
            dto.setExperience(staff.getExperience());
            dto.setStatus(staff.getStatus());

            Double avgRating = reviewRepository.findAverageRatingByStaffId(staff.getId());
            dto.setAverageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public StaffResponseDto updateStaff(Long id, StaffUpdateRequestDto requestDto) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        if (requestDto.getFullName() != null) staff.setFullName(requestDto.getFullName());
        if (requestDto.getPhoto() != null) staff.setPhoto(requestDto.getPhoto());
        if (requestDto.getPhone() != null) staff.setPhone(requestDto.getPhone());
        if (requestDto.getAddress() != null) staff.setAddress(requestDto.getAddress());
        if (requestDto.getRole() != null) staff.setRole(requestDto.getRole());
        if (requestDto.getSkills() != null) staff.setSkills(requestDto.getSkills());
        if (requestDto.getExperience() != null) staff.setExperience(requestDto.getExperience());
        if (requestDto.getJoiningDate() != null) staff.setJoiningDate(requestDto.getJoiningDate());
        if (requestDto.getWorkingHours() != null) staff.setWorkingHours(requestDto.getWorkingHours());
        if (requestDto.getAvailability() != null) staff.setAvailability(requestDto.getAvailability());
        if (requestDto.getStatus() != null) staff.setStatus(requestDto.getStatus());

        Staff updatedStaff = staffRepository.save(staff);
        return mapToStaffResponseDto(updatedStaff);
    }

    @Override
    public StaffResponseDto updateStaffStatus(Long id, StaffStatusUpdateRequestDto requestDto) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        staff.setStatus(requestDto.getStatus());
        Staff updatedStaff = staffRepository.save(staff);
        return mapToStaffResponseDto(updatedStaff);
    }

    private StaffResponseDto mapToStaffResponseDto(Staff staff) {
        StaffResponseDto dto = new StaffResponseDto();
        dto.setId(staff.getId());
        dto.setStaffId(staff.getStaffId());
        dto.setFullName(staff.getFullName());
        dto.setPhoto(staff.getPhoto());
        dto.setPhone(staff.getPhone());
        dto.setAddress(staff.getAddress());
        dto.setRole(staff.getRole());
        dto.setSkills(staff.getSkills());
        dto.setExperience(staff.getExperience());
        dto.setJoiningDate(staff.getJoiningDate());
        dto.setWorkingHours(staff.getWorkingHours());
        dto.setAvailability(staff.getAvailability());
        dto.setStatus(staff.getStatus());
        dto.setCreatedAt(staff.getCreatedAt());
        dto.setUpdatedAt(staff.getUpdatedAt());
        return dto;
    }
}
