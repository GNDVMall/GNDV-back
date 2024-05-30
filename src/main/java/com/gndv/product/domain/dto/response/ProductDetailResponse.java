package com.gndv.product.domain.dto.response;

import com.gndv.constant.Boolean;
import com.gndv.constant.ProductStatus;
import lombok.Getter;

@Getter
public class ProductDetailResponse extends ProductResponse {
    // 판매자 정보
    private String nickname;
    private String introduction;
    private Long rating;

    // 상속된 필드들
    private Long product_id;
    private Long item_id;
    private String title;
    private String content;
    private Long price;
    private Long member_id;
    private Boolean product_trade_opt1;
    private Boolean product_trade_opt2;
    private ProductStatus product_status;
    // 기타 필요한 필드들
}
