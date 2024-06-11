package com.gndv.review.controller;

import com.gndv.common.CustomResponse;
import com.gndv.review.domain.dto.request.ReviewInsertRequest;
import com.gndv.review.domain.dto.response.ReviewDetailResponse;
import com.gndv.review.domain.entity.Review;
import com.gndv.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public CustomResponse<ReviewDetailResponse> createReview(@RequestBody ReviewInsertRequest request) {
        ReviewDetailResponse response = reviewService.createReview(request);
        return CustomResponse.ok("리뷰가 성공적으로 등록되었습니다.", response);
    }

    @GetMapping("/{review_id}")
    public CustomResponse<ReviewDetailResponse> getReviewById(@PathVariable Long review_id) {
        ReviewDetailResponse response = reviewService.getReviewById(review_id);
        return CustomResponse.ok("리뷰를 성공적으로 조회했습니다.", response);
    }

    @GetMapping("/member/{member_id}")
    public CustomResponse<List<ReviewDetailResponse>> getReviewsByMemberId(@PathVariable Long member_id) {
        List<ReviewDetailResponse> responses = reviewService.getReviewsByMemberId(member_id);
        return CustomResponse.ok("멤버의 리뷰 목록을 성공적으로 조회했습니다.", responses);
    }

    @PutMapping("/{review_id}")
    public CustomResponse<Void> updateReview(@PathVariable Long review_id, @RequestBody ReviewInsertRequest request) {
        Review review = Review.builder()
                .review_id(review_id)
                .review_content(request.getReview_content())
                .review_rating(request.getReview_rating())
                .review_type(request.getReview_type())
                .member_id(request.getMember_id())
                .build();
        reviewService.updateReview(review);
        return CustomResponse.ok("리뷰가 성공적으로 업데이트되었습니다.");
    }
}
