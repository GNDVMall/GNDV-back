package com.gndv.payment.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gndv.payment.constain.PaymentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LocalPayRequest {
    private String imp_uid;
    private String order_uid;
    private Long item_id;
    private String item_name;
    private String username;
    private Long payment_price;
    private String email;
    private String address;
    private String payment_uid;
    private String status; // 추가된 필드

    @Builder
    public LocalPayRequest(String order_uid, Long item_id, String item_name, String username, Long payment_price, String email, String address, String payment_uid, String status, String imp_uid) {
        this.order_uid = order_uid;
        this.item_id = item_id;
        this.item_name = item_name;
        this.username = username;
        this.payment_price = payment_price;
        this.email = email;
        this.address = address;
        this.payment_uid = payment_uid;
        this.status = status; // 추가된 필드
        this.imp_uid = imp_uid;
    }
}
