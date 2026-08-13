package com.community.staffbackend.repository;

import com.community.staffbackend.entity.ReplacementStatus;
import com.community.staffbackend.entity.TemporaryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemporaryAssignmentRepository extends JpaRepository<TemporaryAssignment, Long> {

    List<TemporaryAssignment> findByRegularStaffId(Long regularStaffId);

    List<TemporaryAssignment> findByReplacementStaffId(Long replacementStaffId);

    List<TemporaryAssignment> findByStatus(ReplacementStatus status);

    long countByStatus(ReplacementStatus status);
}
