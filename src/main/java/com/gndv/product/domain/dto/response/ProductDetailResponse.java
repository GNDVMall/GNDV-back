package com.gndv.product.domain.dto.response;

import com.gndv.product.domain.entity.Product;
import lombok.Getter;

@Getter
public class ProductDetailResponse extends Product {
    private String image_ids;
}
