package com.gndv.review.service;

import com.gndv.review.domain.dto.request.ReviewInsertRequest;
import com.gndv.review.domain.dto.response.ReviewDetailResponse;
import com.gndv.review.domain.entity.Review;
import com.gndv.review.mapper.ReviewMapper;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.member.domain.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

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
        logger.info("Received createReview request: {}", request); // Log request details

        validateEmail(request.getEmail());

        if (request.getProduct_id() == null) {
            // Fetch product_id using orderListId if not provided
            Long productId = reviewMapper.findProductIdByOrderListId(request.getOrder_list_id());
            if (productId == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }
            request.setProduct_id(productId);
        }

        if (reviewExists(request.getProduct_id(), request.getEmail())) {
            throw new ReviewAlreadyExistsException("You have already reviewed this product.");
        }

        Member member = findMemberByEmail(request.getEmail());

        Review review = Review.builder()
                .review_content(request.getReview_content())
                .review_rating(request.getReview_rating())
                //.review_type(request.getReview_type())
                .email(member.getEmail())
                .product_id(request.getProduct_id())
                .build();

        reviewMapper.save(review);
        return mapToDetailResponse(review);
    }

    @Override
    public ReviewDetailResponse getReviewById(Long review_id) {
        logger.info("Fetching review with ID: {}", review_id);
        Review review = reviewMapper.findById(review_id);
        if (review == null) {
            logger.error("Review not found with ID: {}", review_id);
            throw new ReviewAlreadyExistsException("Review not found with ID: " + review_id);
        }
        return mapToDetailResponse(review);
    }

    @Override
    public List<ReviewDetailResponse> getReviewsByEmail(String email) {
        logger.info("Fetching reviews for email: {}", email);
        Member member = findMemberByEmail(email);

        List<Review> reviews = reviewMapper.findByEmail(member.getEmail());
        return reviews.stream()
                .map(this::mapToDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateReview(ReviewInsertRequest request, Long review_id) {
        logger.info("Updating review with ID: {}", review_id);
        Member member = findMemberByEmail(request.getEmail());

        Long productId = request.getProduct_id() != null ? request.getProduct_id() : reviewMapper.findProductIdByOrderListId(request.getOrder_list_id());

        Review review = Review.builder()
                .review_id(review_id)
                .review_content(request.getReview_content())
                .review_rating(request.getReview_rating())
                //.review_type(request.getReview_type())
                .email(member.getEmail())
                .product_id(productId)
                .build();
        reviewMapper.update(review);
        logger.info("Review updated successfully");
    }

    @Override
    public boolean reviewExists(Long product_id, String email) {
        logger.info("Checking if review exists for product_id: {} and email: {}", product_id, email);
        return reviewMapper.existsByProductIdAndEmail(product_id, email);
    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            logger.error("Email cannot be null or empty");
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
    }

    private Member findMemberByEmail(String email) {
        return memberMapper.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Invalid email: {}", email);
                    return new IllegalArgumentException("Invalid email");
                });
    }

    private ReviewDetailResponse mapToDetailResponse(Review review) {
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
}
