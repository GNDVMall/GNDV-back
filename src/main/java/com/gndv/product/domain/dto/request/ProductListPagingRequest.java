package com.gndv.product.domain.dto.request;

import com.gndv.common.domain.request.PagingRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductListPagingRequest extends PagingRequest {
    private Long item_id;
}
