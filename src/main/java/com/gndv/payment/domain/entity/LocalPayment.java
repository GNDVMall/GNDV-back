package com.gndv.payment.domain.entity;

import com.gndv.payment.constain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true) // toBuilder를 true로 설정
@NoArgsConstructor
@AllArgsConstructor
public class LocalPayment {
    private Long payment_id;
    private Long price;
    private PaymentStatus status;
    private String payment_uid;
}
