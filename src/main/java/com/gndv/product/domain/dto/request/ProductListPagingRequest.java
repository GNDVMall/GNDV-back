package com.gndv.product.domain.dto.request;

import com.gndv.common.domain.request.PagingRequest;
import lombok.Data;

@Data
public class ProductListPagingRequest extends PagingRequest {
    private Long item_id;
}
