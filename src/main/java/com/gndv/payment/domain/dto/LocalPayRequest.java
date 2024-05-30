package com.gndv.payment.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LocalPayRequest {

    private String order_uid;
    private Long item_id;


    private String item_name; // 추가된 속성
    private String username;
    private Long payment_price;
    private String email;
    private String address;
    private String payment_uid;

    @Builder
    public LocalPayRequest(String order_uid, Long item_id, String item_name, String username, Long payment_price, String email, String address, String payment_uid) {
        this.order_uid = order_uid;
        this.item_id = item_id;
        this.item_name = item_name;
        this.username = username;
        this.payment_price = payment_price;
        this.email = email;
        this.address = address;
        this.payment_uid = payment_uid;
    }


}
