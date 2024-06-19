package com.gndv.review.domain.entity;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    private Long review_id;
    private String review_content;
    private Long review_rating;
    private String review_type;
    private Long product_id;
    private Long review_report_count;

    // Member
    private Long member_id;
    private String email;
}
