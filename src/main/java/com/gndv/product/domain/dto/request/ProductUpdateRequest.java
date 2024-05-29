package com.gndv.product.domain.dto.request;

import com.gndv.constant.Boolean;
import com.gndv.constant.ProductStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateRequest {
    private Long member_id; // 판매자 id
    private Long product_id;
    private String title;
    private Long price;
    private String content;
    private ProductStatus product_status;
    private Boolean product_trade_opt1;
    private Boolean product_trade_opt2;
}
