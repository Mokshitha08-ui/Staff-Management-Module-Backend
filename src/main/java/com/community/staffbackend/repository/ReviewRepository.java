package com.community.staffbackend.repository;

import com.community.staffbackend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByStaffId(Long staffId);

    long countByStaffId(Long staffId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.staff.id = :staffId")
    Double findAverageRatingByStaffId(@Param("staffId") Long staffId);

    @Query("SELECT AVG(r.rating) FROM Review r")
    Double findGlobalAverageRating();
}
