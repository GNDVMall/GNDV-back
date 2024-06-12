package com.gndv.review.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewInsertRequest {
    private String review_content;
    private Long review_rating;
    private String review_type;
    private String email;
    private Long product_id;
}
