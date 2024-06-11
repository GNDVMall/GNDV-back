package com.gndv.product.domain.dto.request;

import com.gndv.constant.Boolean;
import com.gndv.constant.ProductSalesStatus;
import com.gndv.constant.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInsertRequest {
    private String email;
    private Long item_id; // 생성하는 item id
    private String title;
    private Long price;
    private String content;
    private ProductStatus product_status;
    private Boolean product_trade_opt1;
    private Boolean product_trade_opt2;
    private Long product_id;
}