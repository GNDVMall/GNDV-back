package com.gndv.review.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewDetailResponse {
    private Long review_id;
    private String review_content;
    private Long review_rating;
    private String review_type;
    private String email;
    private Long product_id;
    private Long review_report_count;
}
