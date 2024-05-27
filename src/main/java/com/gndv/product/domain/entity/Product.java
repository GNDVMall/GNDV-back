package com.gndv.product.domain.entity;

import com.gndv.constant.Boolean;
import lombok.Getter;

@Getter
public class Product {
    private Long product_id;
    private Long item_id;
    private String title;
    private String content;
    private Long price;
    private Long member_id;
    private Boolean trade_option1;
    private Boolean trade_option2;
}
