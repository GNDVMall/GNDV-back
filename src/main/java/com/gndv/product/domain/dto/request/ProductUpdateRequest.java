package com.gndv.product.domain.dto.request;

import com.gndv.constant.Boolean;
import com.gndv.constant.ProductSalesStatus;
import com.gndv.constant.ProductStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductUpdateRequest {
    private String email;
    private Long product_id;
    private String title;
    private Long price;
    private String content;
    private ProductStatus product_status;
    private Boolean product_trade_opt1;
    private Boolean product_trade_opt2;
    private ProductSalesStatus product_sales_status;
    private List<MultipartFile> images;
    private List<String> delete_images;
}
