package com.gndv.review.service;

import com.gndv.review.domain.dto.request.ReviewInsertRequest;
import com.gndv.review.domain.dto.response.ReviewDetailResponse;

import java.util.List;

public interface ReviewService {
    ReviewDetailResponse createReview(ReviewInsertRequest request);
    ReviewDetailResponse getReviewById(Long review_id);
    List<ReviewDetailResponse> getReviewsByEmail(String email);
    void updateReview(ReviewInsertRequest request, Long review_id);
    boolean reviewExists(Long product_id, String email);
}
