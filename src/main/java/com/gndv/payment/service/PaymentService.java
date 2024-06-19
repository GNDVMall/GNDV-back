package com.gndv.payment.service;

import com.gndv.payment.domain.dto.LocalPayRequest;
import com.gndv.payment.domain.entity.LocalPayment;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

public interface PaymentService {
    LocalPayment createPayment(LocalPayRequest request);
    LocalPayment findPaymentById(Long payment_id);
    LocalPayment findPaymentByUid(String payment_uid);

    IamportResponse<Payment> paymentByCallback(LocalPayRequest request);
    void updateProductStatusToSoldOutIfPaid(String orderUid);
}
