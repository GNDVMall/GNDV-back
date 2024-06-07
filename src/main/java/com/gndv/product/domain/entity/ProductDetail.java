package com.gndv.product.domain.entity;

import lombok.Getter;

@Getter
public class ProductDetail extends Product {
    private String[] images;
    private Integer total;
    public void setImages(String images) {
        this.images = images != null ? images.split(",") : new String[0];
    }
}
