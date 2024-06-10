package com.gndv.payment.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCreateRequestDTO {
    private Long product_id;
    private Long price;
    private String item_name;

    @Builder
    public OrderCreateRequestDTO(Long product_id, Long price, String item_name) {
        this.product_id = product_id;
        this.price = price;
        this.item_name = item_name;
    }
}
