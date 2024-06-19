package com.gndv.review.controller;

import com.gndv.common.CustomResponse;
import com.gndv.review.domain.dto.request.ReviewInsertRequest;
import com.gndv.review.domain.dto.response.ReviewDetailResponse;
import com.gndv.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/reviews")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Review API", description = "리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 생성", description = "새로운 리뷰를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 생성 성공", content = @Content(schema = @Schema(implementation = ReviewDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public CustomResponse<ReviewDetailResponse> createReview(
            @Parameter(description = "리뷰 생성 요청 객체", required = true) @RequestBody ReviewInsertRequest request) {
        log.info("Create Review: {}", request);
        ReviewDetailResponse response = reviewService.createReview(request);
        return CustomResponse.ok("Review created successfully", response);
    }

    @Operation(summary = "리뷰 조회", description = "특정 ID를 가진 리뷰를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 조회 성공", content = @Content(schema = @Schema(implementation = ReviewDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{review_id}")
    public CustomResponse<ReviewDetailResponse> getReviewById(
            @Parameter(description = "리뷰 ID", required = true) @PathVariable Long review_id) {
        log.info("Get Review by ID: {}", review_id);
        ReviewDetailResponse response = reviewService.getReviewById(review_id);
        return CustomResponse.ok("Review fetched successfully", response);
    }

    @Operation(summary = "리뷰 수정", description = "특정 ID를 가진 리뷰를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{review_id}")
    public CustomResponse<Void> updateReview(
            @Parameter(description = "리뷰 ID", required = true) @PathVariable Long review_id,
            @Parameter(description = "리뷰 수정 요청 객체", required = true) @RequestBody ReviewInsertRequest request) {
        log.info("Update Review: {} with ID: {}", request, review_id);
        reviewService.updateReview(request, review_id);
        return CustomResponse.ok("Review updated successfully");
    }

    @Operation(summary = "리뷰 존재 여부 확인", description = "특정 상품 ID와 이메일로 리뷰 존재 여부를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 존재 여부 확인 성공", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/check")
    public CustomResponse<Boolean> checkReviewExists(
            @Parameter(description = "상품 ID", required = true) @RequestParam Long productId,
            @Parameter(description = "사용자 이메일", required = true) @RequestParam String email) {
        log.info("Check if Review exists for Product ID: {} and Email: {}", productId, email);
        boolean exists = reviewService.reviewExists(productId, email);
        return CustomResponse.ok("Review existence checked successfully", exists);
    }
}
