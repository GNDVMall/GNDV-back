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
    private Long order_list_id;


    @Override
    public String toString() {
        return "ReviewInsertRequest{" +
                "review_content='" + review_content + '\'' +
                ", review_rating=" + review_rating +
                ", review_type='" + review_type + '\'' +
                ", email='" + email + '\'' +
                ", product_id=" + product_id +
                ", order_list_id=" + order_list_id +
                '}';
    }
}
