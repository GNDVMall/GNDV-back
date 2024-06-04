package com.gndv.payment.service;

import com.gndv.payment.domain.dto.LocalPayRequest;
import com.gndv.payment.domain.entity.LocalPayment;

public interface PaymentService {
    LocalPayment createPayment(LocalPayRequest request);
    LocalPayment findPaymentById(Long paymentId);
    void updatePayment(LocalPayment localPayment);
    void deletePayment(Long paymentId);
}
