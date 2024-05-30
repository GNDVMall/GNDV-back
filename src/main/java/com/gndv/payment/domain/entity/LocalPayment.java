package com.gndv.payment.domain.entity;

import com.gndv.payment.constain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalPayment {

    private Long payment_id;
    private Long price;
    private PaymentStatus status;
    private String payment_uid;
    private Long member_id;

    public void changePaymentBySuccess(PaymentStatus status, String payment_uid) {
        this.status = status;
        this.payment_uid = payment_uid;
    }
}
