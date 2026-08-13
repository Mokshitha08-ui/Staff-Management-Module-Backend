package com.community.staffbackend.repository;

import com.community.staffbackend.entity.Attendance;
import com.community.staffbackend.entity.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByStaffIdAndDate(Long staffId, LocalDate date);

    List<Attendance> findByDate(LocalDate date);

    List<Attendance> findByStaffId(Long staffId);

    long countByDateAndCheckInIsNotNullAndCheckOutIsNull(LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE " +
           "(:date IS NULL OR a.date = :date) AND " +
           "(:staffId IS NULL OR a.staff.id = :staffId) AND " +
           "(:status IS NULL OR a.status = :status)")
    List<Attendance> searchAttendance(@Param("date") LocalDate date,
                                       @Param("staffId") Long staffId,
                                       @Param("status") AttendanceStatus status);
}
