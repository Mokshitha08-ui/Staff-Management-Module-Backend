package com.community.staffbackend.service.impl;

import com.community.staffbackend.dto.request.StaffCreateRequestDto;
import com.community.staffbackend.dto.request.StaffStatusUpdateRequestDto;
import com.community.staffbackend.dto.request.StaffUpdateRequestDto;
import com.community.staffbackend.dto.response.StaffProfileResponseDto;
import com.community.staffbackend.dto.response.StaffResponseDto;
import com.community.staffbackend.entity.Attendance;
import com.community.staffbackend.entity.Schedule;
import com.community.staffbackend.entity.Staff;
import com.community.staffbackend.exception.DuplicateResourceException;
import com.community.staffbackend.exception.ResourceNotFoundException;
import com.community.staffbackend.repository.AttendanceRepository;
import com.community.staffbackend.repository.ScheduleRepository;
import com.community.staffbackend.repository.StaffRepository;
import com.community.staffbackend.service.StaffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final ScheduleRepository scheduleRepository;
    private final AttendanceRepository attendanceRepository;

    public StaffServiceImpl(StaffRepository staffRepository,
                            ScheduleRepository scheduleRepository,
                            AttendanceRepository attendanceRepository) {
        this.staffRepository = staffRepository;
        this.scheduleRepository = scheduleRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffResponseDto> getAllStaff(String query, String category, String status) {
        List<Staff> list = staffRepository.searchAndFilterStaff(
                (query != null && !query.trim().isEmpty()) ? query.trim() : null,
                (category != null && !category.trim().isEmpty()) ? category.trim() : null,
                (status != null && !status.trim().isEmpty()) ? status.trim() : null
        );
        return list.stream().map(this::mapToStaffResponseDto).collect(Collectors.toList());
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
    public StaffResponseDto getStaffByStaffId(String staffId) {
        java.util.Optional<Staff> staffOpt = staffRepository.findByStaffId(staffId);
        if (staffOpt.isPresent()) {
            return mapToStaffResponseDto(staffOpt.get());
        }
        try {
            Long numericId = Long.parseLong(staffId);
            Staff staff = staffRepository.findById(numericId)
                    .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + staffId));
            return mapToStaffResponseDto(staff);
        } catch (NumberFormatException e) {
            throw new ResourceNotFoundException("Staff member not found with Staff ID: " + staffId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public StaffProfileResponseDto getStaffProfile(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        LocalDate today = LocalDate.now();
        Optional<Schedule> todaySchedule = scheduleRepository.findByStaffIdAndDate(id, today);
        String todaysArea = todaySchedule.map(Schedule::getTowerAssigned).orElse(staff.getTowerAssigned());

        StaffProfileResponseDto profile = new StaffProfileResponseDto();
        profile.setId(staff.getId());
        profile.setStaffId(staff.getStaffId());
        profile.setFullName(staff.getFullName());
        profile.setPhoto(staff.getPhoto());
        profile.setPhone(staff.getPhone());
        profile.setAddress(staff.getAddress());
        profile.setRole(staff.getRole() != null ? staff.getRole() : staff.getCategory());
        profile.setSkills(staff.getSkills());
        profile.setExperience(staff.getExperience() != null ? Integer.getInteger(staff.getExperience(), 3) : 3);
        profile.setWorkingHours(staff.getWorkingHours());
        profile.setAvailability(staff.getAvailability());
        profile.setStatus(staff.getStatus());
        profile.setTodaysAssignedArea(todaysArea);

        return profile;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffResponseDto> getOnDutyStaff() {
        return staffRepository.findByIsInsideCommunityTrue()
                .stream()
                .map(this::mapToStaffResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffResponseDto> getPublicStaffList() {
        return staffRepository.findAll()
                .stream()
                .map(this::mapToStaffResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StaffResponseDto getPublicStaffProfile(String staffId) {
        return getStaffByStaffId(staffId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffResponseDto> getDigitalStaffIds() {
        return getAllStaff(null, null, null);
    }

    @Override
    public StaffResponseDto createStaff(StaffCreateRequestDto requestDto) {
        String staffId = requestDto.getStaffId();
        if (staffId == null || staffId.trim().isEmpty()) {
            staffId = "STF-" + String.format("%03d", staffRepository.count() + 1);
        }
        if (staffRepository.existsByStaffId(staffId)) {
            throw new DuplicateResourceException("Staff member with ID '" + staffId + "' already exists");
        }

        Staff staff = new Staff();
        staff.setStaffId(staffId);
        staff.setFullName(requestDto.getFullName() != null ? requestDto.getFullName() : requestDto.getName());
        staff.setPhone(requestDto.getPhone());
        staff.setEmail(requestDto.getEmail());
        staff.setPhoto(requestDto.getPhoto());
        staff.setCategory(requestDto.getCategory() != null ? requestDto.getCategory() : "guard");
        staff.setRole(requestDto.getRole() != null ? requestDto.getRole() : staff.getCategory());
        staff.setBlockAssigned(requestDto.getBlockAssigned());
        staff.setTowerAssigned(requestDto.getTowerAssigned());
        staff.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : "active");
        staff.setVerificationStatus(requestDto.getVerificationStatus() != null ? requestDto.getVerificationStatus() : "pending");
        staff.setVerificationSubmittedDate(LocalDateTime.now());
        staff.setJoinDate(requestDto.getJoinDate() != null ? requestDto.getJoinDate() : LocalDateTime.now());
        staff.setDocumentExpiry(requestDto.getDocumentExpiry() != null ? requestDto.getDocumentExpiry() : LocalDateTime.now().plusYears(1));
        staff.setSkills(requestDto.getSkills());
        staff.setExperience(requestDto.getExperience());
        staff.setWorkingHours(requestDto.getWorkingHours());
        staff.setAddress(requestDto.getAddress());
        staff.setAvailability(requestDto.getAvailability());
        staff.setIdProofType(requestDto.getIdProofType() != null ? requestDto.getIdProofType() : "Aadhaar");
        staff.setEmergencyContactName(requestDto.getEmergencyContactName());
        staff.setEmergencyContactRelation(requestDto.getEmergencyContactRelation());
        staff.setEmergencyContactPhone(requestDto.getEmergencyContactPhone());
        staff.setIsInsideCommunity(false);
        staff.setIsTemporary(false);

        Staff saved = staffRepository.save(staff);
        return mapToStaffResponseDto(saved);
    }

    @Override
    public StaffResponseDto updateStaff(Long id, StaffUpdateRequestDto requestDto) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        if (requestDto.getFullName() != null) staff.setFullName(requestDto.getFullName());
        if (requestDto.getName() != null && requestDto.getFullName() == null) staff.setFullName(requestDto.getName());
        if (requestDto.getPhone() != null) staff.setPhone(requestDto.getPhone());
        if (requestDto.getEmail() != null) staff.setEmail(requestDto.getEmail());
        if (requestDto.getPhoto() != null) staff.setPhoto(requestDto.getPhoto());
        if (requestDto.getCategory() != null) staff.setCategory(requestDto.getCategory());
        if (requestDto.getRole() != null) staff.setRole(requestDto.getRole());
        if (requestDto.getBlockAssigned() != null) staff.setBlockAssigned(requestDto.getBlockAssigned());
        if (requestDto.getTowerAssigned() != null) staff.setTowerAssigned(requestDto.getTowerAssigned());
        if (requestDto.getStatus() != null) staff.setStatus(requestDto.getStatus());
        if (requestDto.getVerificationStatus() != null) staff.setVerificationStatus(requestDto.getVerificationStatus());
        if (requestDto.getJoinDate() != null) staff.setJoinDate(requestDto.getJoinDate());
        if (requestDto.getDocumentExpiry() != null) staff.setDocumentExpiry(requestDto.getDocumentExpiry());
        if (requestDto.getIsInsideCommunity() != null) staff.setIsInsideCommunity(requestDto.getIsInsideCommunity());
        if (requestDto.getSkills() != null) staff.setSkills(requestDto.getSkills());
        if (requestDto.getExperience() != null) staff.setExperience(requestDto.getExperience());
        if (requestDto.getWorkingHours() != null) staff.setWorkingHours(requestDto.getWorkingHours());
        if (requestDto.getAddress() != null) staff.setAddress(requestDto.getAddress());
        if (requestDto.getAvailability() != null) staff.setAvailability(requestDto.getAvailability());
        if (requestDto.getIdProofType() != null) staff.setIdProofType(requestDto.getIdProofType());
        if (requestDto.getEmergencyContactName() != null) staff.setEmergencyContactName(requestDto.getEmergencyContactName());
        if (requestDto.getEmergencyContactRelation() != null) staff.setEmergencyContactRelation(requestDto.getEmergencyContactRelation());
        if (requestDto.getEmergencyContactPhone() != null) staff.setEmergencyContactPhone(requestDto.getEmergencyContactPhone());

        Staff updated = staffRepository.save(staff);
        return mapToStaffResponseDto(updated);
    }

    @Override
    public StaffResponseDto updateStaffStatus(Long id, StaffStatusUpdateRequestDto requestDto) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        staff.setStatus(requestDto.getStatus());
        Staff updated = staffRepository.save(staff);
        return mapToStaffResponseDto(updated);
    }

    @Override
    public void deleteStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));
        staffRepository.delete(staff);
    }

    @Override
    public StaffResponseDto checkInStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        staff.setIsInsideCommunity(true);
        staff.setLastCheckIn(LocalDateTime.now());
        Staff updated = staffRepository.save(staff);

        // Also record Attendance entry
        Attendance attendance = new Attendance();
        attendance.setStaff(updated);
        attendance.setDate(LocalDate.now());
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setStatus("present");
        attendance.setGate("Main Gate");
        attendanceRepository.save(attendance);

        return mapToStaffResponseDto(updated);
    }

    @Override
    public StaffResponseDto checkOutStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        staff.setIsInsideCommunity(false);
        staff.setLastCheckOut(LocalDateTime.now());
        Staff updated = staffRepository.save(staff);

        // Update Attendance record check-out time
        Optional<Attendance> todayAtt = attendanceRepository.findByStaffIdAndDate(id, LocalDate.now());
        if (todayAtt.isPresent()) {
            Attendance att = todayAtt.get();
            att.setCheckOutTime(LocalDateTime.now());
            if (att.getCheckInTime() != null) {
                long minutes = java.time.Duration.between(att.getCheckInTime(), att.getCheckOutTime()).toMinutes();
                att.setTotalHours(String.format("%.1f", minutes / 60.0));
            }
            attendanceRepository.save(att);
        }

        return mapToStaffResponseDto(updated);
    }

    @Override
    public StaffResponseDto updateVerificationStatus(Long id, String verificationStatus) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        staff.setVerificationStatus(verificationStatus);
        Staff updated = staffRepository.save(staff);
        return mapToStaffResponseDto(updated);
    }

    @Override
    public StaffResponseDto markStaffOnLeave(Long id, LocalDateTime startDate, LocalDateTime endDate) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        staff.setStatus("on_leave");
        staff.setLeaveStartDate(startDate != null ? startDate : LocalDateTime.now());
        staff.setLeaveEndDate(endDate);
        Staff updated = staffRepository.save(staff);
        return mapToStaffResponseDto(updated);
    }

    @Override
    public StaffResponseDto suspendStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        staff.setStatus("suspended");
        staff.setIsInsideCommunity(false);
        Staff updated = staffRepository.save(staff);
        return mapToStaffResponseDto(updated);
    }

    @Override
    public StaffResponseDto activateStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        staff.setStatus("active");
        Staff updated = staffRepository.save(staff);
        return mapToStaffResponseDto(updated);
    }

    @Override
    public StaffResponseDto deactivateStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + id));

        staff.setStatus("inactive");
        staff.setIsInsideCommunity(false);
        Staff updated = staffRepository.save(staff);
        return mapToStaffResponseDto(updated);
    }

    @Override
    public StaffResponseDto markUnavailable(Long id) {
        return suspendStaff(id);
    }

    private StaffResponseDto mapToStaffResponseDto(Staff staff) {
        StaffResponseDto dto = new StaffResponseDto();
        dto.setId(staff.getId());
        dto.setStaffId(staff.getStaffId());
        dto.setFullName(staff.getFullName());
        dto.setName(staff.getFullName());
        dto.setPhone(staff.getPhone());
        dto.setEmail(staff.getEmail());
        dto.setPhoto(staff.getPhoto());
        dto.setCategory(staff.getCategory() != null ? staff.getCategory() : "guard");
        dto.setRole(staff.getRole() != null ? staff.getRole() : staff.getCategory());
        dto.setBlockAssigned(staff.getBlockAssigned());
        dto.setTowerAssigned(staff.getTowerAssigned());
        dto.setStatus(staff.getStatus());
        dto.setVerificationStatus(staff.getVerificationStatus());
        dto.setVerificationSubmittedDate(staff.getVerificationSubmittedDate());
        dto.setJoinDate(staff.getJoinDate());
        dto.setDocumentExpiry(staff.getDocumentExpiry());
        dto.setIsInsideCommunity(staff.getIsInsideCommunity());
        dto.setLastCheckIn(staff.getLastCheckIn());
        dto.setLastCheckOut(staff.getLastCheckOut());
        dto.setLeaveStartDate(staff.getLeaveStartDate());
        dto.setLeaveEndDate(staff.getLeaveEndDate());
        dto.setIsTemporary(staff.getIsTemporary());
        dto.setSkills(staff.getSkills());
        dto.setExperience(staff.getExperience());
        dto.setWorkingHours(staff.getWorkingHours());
        dto.setAddress(staff.getAddress());
        dto.setAvailability(staff.getAvailability());
        dto.setIdProofType(staff.getIdProofType());
        dto.setEmergencyContactName(staff.getEmergencyContactName());
        dto.setEmergencyContactRelation(staff.getEmergencyContactRelation());
        dto.setEmergencyContactPhone(staff.getEmergencyContactPhone());
        dto.setCreatedAt(staff.getCreatedAt());
        dto.setUpdatedAt(staff.getUpdatedAt());
        return dto;
    }
}
