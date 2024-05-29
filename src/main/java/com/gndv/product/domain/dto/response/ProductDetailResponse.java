package com.gndv.product.domain.dto.response;

import lombok.Getter;

@Getter
public class ProductDetailResponse extends ProductResponse {
    // 판매자 정보
    private String nickname;
    private String introduction;
    private Long rating;
}
