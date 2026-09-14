package com.community.staffbackend.repository;

import com.community.staffbackend.entity.TemporaryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemporaryAssignmentRepository extends JpaRepository<TemporaryAssignment, Long> {

    List<TemporaryAssignment> findByStatus(String status);

    long countByStatus(String status);

    @Query("SELECT t FROM TemporaryAssignment t WHERE " +
           "(CAST(:search AS string) IS NULL OR :search = '' OR LOWER(t.regularStaff.fullName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR LOWER(t.replacementStaff.fullName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR LOWER(t.reason) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))) AND " +
           "(CAST(:status AS string) IS NULL OR :status = '' OR LOWER(t.status) = LOWER(CAST(:status AS string)))")
    List<TemporaryAssignment> searchAssignments(@Param("search") String search,
                                                @Param("status") String status);
}
