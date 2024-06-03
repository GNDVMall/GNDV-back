package com.gndv.product.domain.entity;

import com.gndv.constant.Boolean;
import com.gndv.constant.ProductStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Product {
    private Long product_id;
    private Long item_id;
    private String title;
    private String content;
    private Long price;
    private Long member_id;
    private Boolean product_trade_opt1;
    private Boolean product_trade_opt2;
    private ProductStatus product_status;
    private LocalDateTime created_at;
}