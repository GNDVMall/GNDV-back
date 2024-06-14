package com.gndv.payment.domain.entity;

import com.gndv.member.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    private Long order_id;
    private String order_uid;
    private Long price;
    private String item_name;
    private Long payment_id;
    private Long buyer_id;
    private Long seller_id;
    private Long product_id;
    private Member buyer;
    private Member seller;
    private LocalPayment payment;
    private Long review_id;
    private String item_image; // 필드 추가
}
