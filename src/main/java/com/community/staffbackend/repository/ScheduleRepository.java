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
           "(CAST(:staffId AS string) IS NULL OR :staffId = '' OR s.staff.staffId = :staffId OR CAST(s.staff.id AS string) = :staffId) AND " +
           "(CAST(:date AS string) IS NULL OR s.date = :date) AND " +
           "(CAST(:shift AS string) IS NULL OR :shift = '' OR LOWER(s.shift) = LOWER(CAST(:shift AS string)))")
    List<Schedule> searchSchedules(@Param("staffId") String staffId,
                                   @Param("date") LocalDate date,
                                   @Param("shift") String shift);
}
