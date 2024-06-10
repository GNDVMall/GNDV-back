package com.gndv.review.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Long review_id;
    private String review_content;
    private Long review_rating;
    private Long review_report_count;
    private String review_type;
    private Long member_id;
}
