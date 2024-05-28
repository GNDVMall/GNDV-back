package com.gndv.product.domain.dto.response;

import com.gndv.product.domain.entity.Product;
import lombok.Getter;

@Getter
public class ProductDetailResponse extends Product {
    private String[] images; // 판매 이미지들

    // 판매자 정보
    private String nickname;
    private String introduction;
    private Long rating;

    public void setImages(String images) {
        this.images = images.split(",");
    }
}
