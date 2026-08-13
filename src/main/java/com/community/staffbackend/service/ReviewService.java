package com.community.staffbackend.service;

import com.community.staffbackend.dto.request.ReviewCreateRequestDto;
import com.community.staffbackend.dto.request.ReviewUpdateRequestDto;
import com.community.staffbackend.dto.response.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto createReview(ReviewCreateRequestDto requestDto);
    List<ReviewResponseDto> getAllReviews();
    List<ReviewResponseDto> getReviewsByStaffId(Long staffId);
    ReviewResponseDto updateReview(Long id, ReviewUpdateRequestDto requestDto);
}
