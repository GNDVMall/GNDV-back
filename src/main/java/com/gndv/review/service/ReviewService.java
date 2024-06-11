package com.gndv.review.service;

import com.gndv.review.domain.dto.request.ReviewInsertRequest;
import com.gndv.review.domain.dto.response.ReviewDetailResponse;
import com.gndv.review.domain.entity.Review;

import java.util.List;

public interface ReviewService {
    ReviewDetailResponse createReview(ReviewInsertRequest request);
    ReviewDetailResponse getReviewById(Long review_id);
    List<ReviewDetailResponse> getReviewsByMemberId(Long member_id);
    void updateReview(Review review);
}
