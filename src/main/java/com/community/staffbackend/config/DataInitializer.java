package com.community.staffbackend.config;

import com.community.staffbackend.entity.*;
import com.community.staffbackend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StaffRepository staffRepository;
    private final AttendanceRepository attendanceRepository;
    private final ScheduleRepository scheduleRepository;
    private final TemporaryAssignmentRepository assignmentRepository;

    public DataInitializer(StaffRepository staffRepository,
                           AttendanceRepository attendanceRepository,
                           ScheduleRepository scheduleRepository,
                           TemporaryAssignmentRepository assignmentRepository) {
        this.staffRepository = staffRepository;
        this.attendanceRepository = attendanceRepository;
        this.scheduleRepository = scheduleRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Application must start completely empty with 0 seeded data
        return;
    }

    private Staff createStaff(String staffId, String name, String phone, String email, String photo, String category, String tower, String block, String status, String verificationStatus, boolean isInside) {
        Staff s = new Staff();
        s.setStaffId(staffId);
        s.setFullName(name);
        s.setPhone(phone);
        s.setEmail(email);
        s.setPhoto(photo);
        s.setCategory(category);
        s.setRole(category);
        s.setTowerAssigned(tower);
        s.setBlockAssigned(block);
        s.setStatus(status);
        s.setVerificationStatus(verificationStatus);
        s.setIsInsideCommunity(isInside);
        s.setJoinDate(LocalDateTime.now().minusMonths(6));
        s.setDocumentExpiry(LocalDateTime.now().plusYears(1));
        s.setVerificationSubmittedDate(LocalDateTime.now().minusMonths(6));
        return staffRepository.save(s);
    }

    private void createAttendance(String code, Staff staff, LocalDate date, LocalDateTime checkIn, LocalDateTime checkOut, String hours, String status, String start, String end, String gate) {
        Attendance a = new Attendance();
        a.setAttendanceCode(code);
        a.setStaff(staff);
        a.setDate(date);
        a.setCheckInTime(checkIn);
        a.setCheckOutTime(checkOut);
        a.setTotalHours(hours);
        a.setStatus(status);
        a.setShiftStart(start);
        a.setShiftEnd(end);
        a.setGate(gate);
        attendanceRepository.save(a);
    }

    private void createSchedule(String code, Staff staff, LocalDate date, String shift, String start, String end, String tower, String block) {
        Schedule sch = new Schedule();
        sch.setScheduleCode(code);
        sch.setStaff(staff);
        sch.setDate(date);
        sch.setShift(shift);
        sch.setStartTime(start);
        sch.setEndTime(end);
        sch.setTowerAssigned(tower);
        sch.setBlockAssigned(block);
        scheduleRepository.save(sch);
    }

    private void createAssignment(String code, Staff regular, Staff replacement, String phone, boolean isTemp, String reason, String tower, String block, String shift, LocalDate start, LocalDate end, boolean untilNotice, String status) {
        TemporaryAssignment ta = new TemporaryAssignment();
        ta.setAssignmentCode(code);
        ta.setRegularStaff(regular);
        ta.setReplacementStaff(replacement);
        ta.setReplacementPhone(phone);
        ta.setIsTemporaryWorker(isTemp);
        ta.setReason(reason);
        ta.setTowerAssigned(tower);
        ta.setBlockAssigned(block);
        ta.setShift(shift);
        ta.setStartDate(start);
        ta.setEndDate(end);
        ta.setIsUntilFurtherNotice(untilNotice);
        ta.setStatus(status);
        assignmentRepository.save(ta);
    }
}
