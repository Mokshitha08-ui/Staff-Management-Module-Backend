package com.community.staffbackend.repository;

import com.community.staffbackend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByStaffIdAndDate(Long staffId, LocalDate date);

    List<Schedule> findByDate(LocalDate date);

    List<Schedule> findByStaffId(Long staffId);

    @Query("SELECT s FROM Schedule s WHERE " +
           "(:date IS NULL OR s.date = :date) AND " +
           "(:staffId IS NULL OR s.staff.id = :staffId) AND " +
           "(:assignedArea IS NULL OR LOWER(s.assignedArea) LIKE LOWER(CONCAT('%', :assignedArea, '%')))")
    List<Schedule> searchSchedules(@Param("date") LocalDate date,
                                   @Param("staffId") Long staffId,
                                   @Param("assignedArea") String assignedArea);
}
