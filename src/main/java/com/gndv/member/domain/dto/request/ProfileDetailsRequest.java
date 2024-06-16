package com.gndv.member.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDetailsRequest {

    private Long review_id;
    private String review_content;
    private Long review_rating;
    private Long review_report_count;
    private String review_type;
    private Long product_id;
    private Long reviewer_id;
    private String reviewer_email;
    private String reviewer_nickname;
    private String review_created_at;
    private String product_title;
    private String product_images;
}
