package com.community.staffbackend.repository;

import com.community.staffbackend.entity.Complaint;
import com.community.staffbackend.entity.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByStaffId(Long staffId);

    long countByStatus(ComplaintStatus status);

    @Query("SELECT c FROM Complaint c WHERE " +
           "(:staffId IS NULL OR c.staff.id = :staffId) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:date IS NULL OR CAST(c.complaintDate AS date) = :date)")
    List<Complaint> searchComplaints(@Param("staffId") Long staffId,
                                      @Param("status") ComplaintStatus status,
                                      @Param("date") LocalDate date);
}
