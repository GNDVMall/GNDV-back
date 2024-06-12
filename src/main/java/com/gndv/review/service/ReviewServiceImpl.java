package com.gndv.review.service;

import com.gndv.review.domain.dto.request.ReviewInsertRequest;
import com.gndv.review.domain.dto.response.ReviewDetailResponse;
import com.gndv.review.domain.entity.Review;
import com.gndv.review.mapper.ReviewMapper;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.member.domain.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final MemberMapper memberMapper;

    @Autowired
    public ReviewServiceImpl(ReviewMapper reviewMapper, MemberMapper memberMapper) {
        this.reviewMapper = reviewMapper;
        this.memberMapper = memberMapper;
    }

    @Override
    @Transactional
    public ReviewDetailResponse createReview(ReviewInsertRequest request) {
        if (request.getEmail() == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        Member member = memberMapper.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        Review review = Review.builder()
                .review_content(request.getReview_content())
                .review_rating(request.getReview_rating())
                .review_type(request.getReview_type())
                .email(member.getEmail())
                .product_id(request.getProduct_id())
                .build();

        reviewMapper.save(review);
        return new ReviewDetailResponse(
                review.getReview_id(),
                review.getReview_content(),
                review.getReview_rating(),
                review.getReview_type(),
                review.getEmail(),
                review.getProduct_id(),
                review.getReview_report_count()
        );
    }

    @Override
    public ReviewDetailResponse getReviewById(Long review_id) {
        Review review = reviewMapper.findById(review_id);
        return new ReviewDetailResponse(
                review.getReview_id(),
                review.getReview_content(),
                review.getReview_rating(),
                review.getReview_type(),
                review.getEmail(),
                review.getProduct_id(),
                review.getReview_report_count()
        );
    }

    @Override
    public List<ReviewDetailResponse> getReviewsByEmail(String email) {
        Member member = memberMapper.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        List<Review> reviews = reviewMapper.findByEmail(member.getEmail());
        return reviews.stream()
                .map(review -> new ReviewDetailResponse(
                        review.getReview_id(),
                        review.getReview_content(),
                        review.getReview_rating(),
                        review.getReview_type(),
                        review.getEmail(),
                        review.getProduct_id(),
                        review.getReview_report_count()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void updateReview(ReviewInsertRequest request, Long review_id) {
        Member member = memberMapper.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        Review review = Review.builder()
                .review_id(review_id)
                .review_content(request.getReview_content())
                .review_rating(request.getReview_rating())
                .review_type(request.getReview_type())
                .email(member.getEmail())
                .product_id(request.getProduct_id())
                .build();
        reviewMapper.update(review);
    }

    @Override
    public boolean reviewExists(Long product_id, String email) {
        return reviewMapper.existsByProductIdAndEmail(product_id, email);
    }

}
