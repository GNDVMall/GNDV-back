package com.gndv.review.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Review {
    private Long review_id;
    private String review_content;
    private Long review_rating;
    private String review_type;
    private String email;
    private Long product_id;
    private Long review_report_count; // 추가된 필드
}
