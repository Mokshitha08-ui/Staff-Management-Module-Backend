package com.community.staffbackend.repository;

import com.community.staffbackend.entity.Attendance;
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

    List<Attendance> findAllByOrderByCheckInTimeDesc();

    @Query("SELECT a FROM Attendance a WHERE " +
           "(CAST(:staffId AS string) IS NULL OR :staffId = '' OR a.staff.staffId = :staffId OR CAST(a.staff.id AS string) = :staffId) AND " +
           "(CAST(:date AS string) IS NULL OR a.date = :date) AND " +
           "(CAST(:status AS string) IS NULL OR :status = '' OR LOWER(a.status) = LOWER(CAST(:status AS string))) AND " +
           "(CAST(:search AS string) IS NULL OR :search = '' OR LOWER(a.staff.fullName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR LOWER(a.staff.staffId) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR LOWER(a.staff.category) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))")
    List<Attendance> searchAttendance(@Param("staffId") String staffId,
                                       @Param("date") LocalDate date,
                                       @Param("status") String status,
                                       @Param("search") String search);
}
