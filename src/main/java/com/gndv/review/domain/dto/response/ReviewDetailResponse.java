package com.gndv.review.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewDetailResponse {
    private Long review_id;
    private String review_content;
    private Long review_rating;
    private Long review_report_count;
    private String review_type;
    private Long member_id;
}
