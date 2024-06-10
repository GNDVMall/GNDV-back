package com.gndv.product.domain.dto.request;

import lombok.Data;

@Data
public class ProductInsertWithMemberRequest {
    private Long product_id;
    private Long member_id;
    private String title;
    private Integer price;
}