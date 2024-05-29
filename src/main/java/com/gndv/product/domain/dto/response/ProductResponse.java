package com.gndv.product.domain.dto.response;

import com.gndv.product.domain.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponse extends Product {
    private String[] images;

    public void setImages(String images) {
        this.images = images != null ? images.split(",") : new String[0];
    }
}
