package com.community.staffbackend.controller;

import com.community.staffbackend.dto.request.ReviewCreateRequestDto;
import com.community.staffbackend.dto.request.ReviewUpdateRequestDto;
import com.community.staffbackend.dto.response.ReviewResponseDto;
import com.community.staffbackend.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        List<ReviewResponseDto> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByStaffId(@PathVariable Long staffId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByStaffId(staffId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@Valid @RequestBody ReviewCreateRequestDto requestDto) {
        ReviewResponseDto created = reviewService.createReview(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewUpdateRequestDto requestDto) {
        ReviewResponseDto updated = reviewService.updateReview(id, requestDto);
        return ResponseEntity.ok(updated);
    }
}
