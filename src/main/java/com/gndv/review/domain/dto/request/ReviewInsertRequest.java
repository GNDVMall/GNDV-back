package com.gndv.review.domain.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewInsertRequest {
    private String review_content;
    private Long review_rating;
    private String review_type;
    private Long member_id;

    @Builder
    public ReviewInsertRequest(String review_content, Long review_rating, String review_type, Long member_id) {
        this.review_content = review_content;
        this.review_rating = review_rating;
        this.review_type = review_type;
        this.member_id = member_id;
    }
}
