package com.gndv.review.controller;

import com.gndv.common.CustomResponse;
import com.gndv.review.domain.dto.request.ReviewInsertRequest;
import com.gndv.review.domain.dto.response.ReviewDetailResponse;
import com.gndv.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public CustomResponse<ReviewDetailResponse> createReview(@RequestBody ReviewInsertRequest request) {
        System.out.println("here"+request);
        ReviewDetailResponse response = reviewService.createReview(request);
        return CustomResponse.ok("Review created successfully", response);
    }

    @GetMapping("/{review_id}")
    public CustomResponse<ReviewDetailResponse> getReviewById(@PathVariable Long review_id) {
        ReviewDetailResponse response = reviewService.getReviewById(review_id);
        return CustomResponse.ok("Review fetched successfully", response);
    }

    @PutMapping("/{review_id}")
    public CustomResponse<Void> updateReview(@PathVariable Long review_id, @RequestBody ReviewInsertRequest request) {
        reviewService.updateReview(request, review_id);
        return CustomResponse.ok("Review updated successfully");
    }

    @GetMapping("/check")
    public CustomResponse<Boolean> checkReviewExists(@RequestParam Long productId, @RequestParam String email) {
        boolean exists = reviewService.reviewExists(productId, email);
        return CustomResponse.ok("Review existence checked successfully", exists);
    }
}