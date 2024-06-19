package com.gndv.payment.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderList {
    private Long order_list_id;
    private Long order_id;
    private Long payment_id;
    private Long seller_id;
    private Long buyer_id;
    private String order_uid; // 새로운 필드 추가

    @Builder
    public OrderList(Long order_list_id, Long order_id, Long payment_id, Long seller_id, Long buyer_id, String order_uid) {
        this.order_list_id = order_list_id;
        this.order_id = order_id;
        this.payment_id = payment_id;
        this.seller_id = seller_id;
        this.buyer_id = buyer_id;
        this.order_uid = order_uid;
    }
}
