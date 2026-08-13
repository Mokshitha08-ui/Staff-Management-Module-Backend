package com.community.staffbackend.repository;

import com.community.staffbackend.entity.Staff;
import com.community.staffbackend.entity.StaffStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    boolean existsByStaffId(String staffId);

    Optional<Staff> findByStaffId(String staffId);

    List<Staff> findByStatus(StaffStatus status);

    List<Staff> findByRoleIgnoreCase(String role);

    @Query("SELECT s FROM Staff s WHERE " +
           "(:query IS NULL OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.staffId) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.skills) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:role IS NULL OR LOWER(s.role) = LOWER(:role)) AND " +
           "(:status IS NULL OR s.status = :status)")
    List<Staff> searchAndFilterStaff(@Param("query") String query,
                                     @Param("role") String role,
                                     @Param("status") StaffStatus status);

    long countByStatus(StaffStatus status);
}
