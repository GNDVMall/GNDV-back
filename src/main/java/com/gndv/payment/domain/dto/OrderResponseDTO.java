package com.gndv.payment.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseDTO {
    private String order_uid;
    private String item_name;
    private Long price;
    private String buyer_name;
    private String buyer_email;
    private String buyer_tel;
    private String buyer_postcode;
    private String paymentStatus;
    private Long paymentPrice;
    private String item_image; // 이미지 URL을 위한 필드 추가
}
