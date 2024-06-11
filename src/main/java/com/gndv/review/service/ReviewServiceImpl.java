package com.gndv.review.service;

import com.gndv.review.domain.dto.request.ReviewInsertRequest;
import com.gndv.review.domain.dto.response.ReviewDetailResponse;
import com.gndv.review.domain.entity.Review;
import com.gndv.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewDetailResponse createReview(ReviewInsertRequest request) {
        Review review = Review.builder()
                .review_content(request.getReview_content())
                .review_rating(request.getReview_rating())
                .review_type(request.getReview_type())
                .member_id(request.getMember_id())
                .build();

        reviewMapper.save(review);

        return ReviewDetailResponse.builder()
                .review_id(review.getReview_id())
                .review_content(review.getReview_content())
                .review_rating(review.getReview_rating())
                .review_report_count(review.getReview_report_count())
                .review_type(review.getReview_type())
                .member_id(review.getMember_id())
                .build();
    }

    @Override
    public ReviewDetailResponse getReviewById(Long review_id) {
        Review review = reviewMapper.findById(review_id);
        return ReviewDetailResponse.builder()
                .review_id(review.getReview_id())
                .review_content(review.getReview_content())
                .review_rating(review.getReview_rating())
                .review_report_count(review.getReview_report_count())
                .review_type(review.getReview_type())
                .member_id(review.getMember_id())
                .build();
    }

    @Override
    public List<ReviewDetailResponse> getReviewsByMemberId(Long member_id) {
        List<Review> reviews = reviewMapper.findByMemberId(member_id);
        return reviews.stream().map(review -> ReviewDetailResponse.builder()
                .review_id(review.getReview_id())
                .review_content(review.getReview_content())
                .review_rating(review.getReview_rating())
                .review_report_count(review.getReview_report_count())
                .review_type(review.getReview_type())
                .member_id(review.getMember_id())
                .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateReview(Review review) {
        reviewMapper.update(review);
    }
}
