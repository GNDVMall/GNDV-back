package com.gndv.product.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RecentProductResponse {
    private Long product_id;
    private Long item_id;
    private String title;
    private String content;
    private Long price;
    private Long member_id;
    private String product_trade_opt1;
    private String product_trade_opt2;
    private String product_status;
    private LocalDateTime created_at;
    private String[] images; // 판매 이미지들

    public void setImages(String images) {
        this.images = images != null ? images.split(",") : new String[0];
    }
}
