package com.community.staffbackend.service.impl;

import com.community.staffbackend.dto.request.ReviewCreateRequestDto;
import com.community.staffbackend.dto.request.ReviewUpdateRequestDto;
import com.community.staffbackend.dto.response.ReviewResponseDto;
import com.community.staffbackend.entity.Review;
import com.community.staffbackend.entity.Staff;
import com.community.staffbackend.exception.InvalidOperationException;
import com.community.staffbackend.exception.ResourceNotFoundException;
import com.community.staffbackend.repository.ReviewRepository;
import com.community.staffbackend.repository.StaffRepository;
import com.community.staffbackend.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final StaffRepository staffRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, StaffRepository staffRepository) {
        this.reviewRepository = reviewRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public ReviewResponseDto createReview(ReviewCreateRequestDto requestDto) {
        if (requestDto.getRating() < 1 || requestDto.getRating() > 5) {
            throw new InvalidOperationException("Rating must be between 1 and 5");
        }

        Staff staff = staffRepository.findById(requestDto.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + requestDto.getStaffId()));

        Review review = new Review();
        review.setStaff(staff);
        review.setRating(requestDto.getRating());
        review.setReviewText(requestDto.getReviewText());
        review.setReviewerName(requestDto.getReviewerName() != null ? requestDto.getReviewerName() : "Anonymous");

        Review saved = reviewRepository.save(review);
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByStaffId(Long staffId) {
        if (!staffRepository.existsById(staffId)) {
            throw new ResourceNotFoundException("Staff member not found with ID: " + staffId);
        }
        return reviewRepository.findByStaffId(staffId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponseDto updateReview(Long id, ReviewUpdateRequestDto requestDto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));

        if (requestDto.getRating() != null) {
            if (requestDto.getRating() < 1 || requestDto.getRating() > 5) {
                throw new InvalidOperationException("Rating must be between 1 and 5");
            }
            review.setRating(requestDto.getRating());
        }
        if (requestDto.getReviewText() != null) review.setReviewText(requestDto.getReviewText());
        if (requestDto.getStatus() != null) review.setStatus(requestDto.getStatus());

        Review updated = reviewRepository.save(review);
        return mapToResponseDto(updated);
    }

    private ReviewResponseDto mapToResponseDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setStaffId(review.getStaff().getId());
        dto.setStaffName(review.getStaff().getFullName());
        dto.setRating(review.getRating());
        dto.setReviewText(review.getReviewText());
        dto.setReviewerName(review.getReviewerName());
        dto.setReviewDate(review.getReviewDate());
        dto.setStatus(review.getStatus());
        return dto;
    }
}
