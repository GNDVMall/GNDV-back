package com.gndv.payment.service;

import com.gndv.payment.constain.PaymentStatus;
import com.gndv.payment.domain.dto.LocalPayRequest;
import com.gndv.payment.domain.entity.LocalPayment;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.mapper.OrderMapper;
import com.gndv.payment.mapper.PaymentMapper;
import com.gndv.payment.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper; // assuming you have a PaymentMapper to interact with the database
    private final IamportClient iamportClient; // assuming you have an Iamport client for API calls

    @Override
    @Transactional
    public LocalPayment createPayment(LocalPayRequest request) {
        LocalPayment payment = LocalPayment.builder()
                .price(request.getPayment_price())
                .status(PaymentStatus.valueOf(request.getStatus().toUpperCase())) // 상태 설정
                .payment_uid(request.getPayment_uid())
                .member_id(request.getMember_id()) // assuming the request contains member_id
                .build();

        paymentMapper.save(payment);
        return payment;
    }

    @Override
    public LocalPayment findPaymentById(Long payment_id) {
        return paymentMapper.findById(payment_id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for id: " + payment_id));
    }

    @Override
    public LocalPayment findPaymentByUid(String payment_uid) {
        return paymentMapper.findByUid(payment_uid)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for UID: " + payment_uid));
    }

    @Override
    @Transactional
    public void updatePayment(LocalPayment payment) {
        paymentMapper.update(payment);
    }

    @Override
    @Transactional
    public void deletePayment(Long payment_id) {
        paymentMapper.delete(payment_id);
    }

    @Override
    public IamportResponse<Payment> paymentByCallback(LocalPayRequest request) {
        try {
            if (request.getImp_uid() == null) {
                throw new IllegalArgumentException("imp_uid must not be null");
            }
            return iamportClient.paymentByImpUid(request.getImp_uid());
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException("아임포트 결제 검증 실패: " + e.getMessage(), e);
        }
    }
}

