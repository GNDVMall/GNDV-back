package com.gndv.payment.service;

import com.gndv.payment.domain.dto.LocalPayRequest;
import com.gndv.payment.domain.entity.LocalPayment;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

public interface PaymentService {
    LocalPayment createPayment(LocalPayRequest request);
    LocalPayment findPaymentById(Long paymentId);
    void updatePayment(LocalPayment localPayment);
    void deletePayment(Long paymentId);
    IamportResponse<Payment> paymentByCallback(LocalPayRequest request);
}
