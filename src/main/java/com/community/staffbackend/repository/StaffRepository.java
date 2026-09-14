package com.community.staffbackend.repository;

import com.community.staffbackend.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    boolean existsByStaffId(String staffId);

    Optional<Staff> findByStaffId(String staffId);

    List<Staff> findByStatus(String status);

    List<Staff> findByIsInsideCommunityTrue();

    long countByStatus(String status);

    long countByIsInsideCommunityTrue();

    long countByJoinDateAfter(LocalDateTime date);

    @Query("SELECT COUNT(s) FROM Staff s WHERE s.documentExpiry IS NOT NULL AND s.documentExpiry <= :targetDate")
    long countExpiringDocuments(@Param("targetDate") LocalDateTime targetDate);

    @Query("SELECT s FROM Staff s WHERE " +
           "(CAST(:query AS string) IS NULL OR :query = '' OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', CAST(:query AS string), '%')) OR LOWER(s.staffId) LIKE LOWER(CONCAT('%', CAST(:query AS string), '%')) OR LOWER(s.category) LIKE LOWER(CONCAT('%', CAST(:query AS string), '%'))) AND " +
           "(CAST(:category AS string) IS NULL OR :category = '' OR LOWER(s.category) = LOWER(CAST(:category AS string))) AND " +
           "(CAST(:status AS string) IS NULL OR :status = '' OR s.status = :status)")
    List<Staff> searchAndFilterStaff(@Param("query") String query,
                                     @Param("category") String category,
                                     @Param("status") String status);
}
