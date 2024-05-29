package com.gndv.product.domain.dto.response;

import com.gndv.product.domain.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponse extends Product {
    private String[] images; // 판매 이미지들
    public void setImages(String images) {
        this.images = images.split(",");
    }
}
