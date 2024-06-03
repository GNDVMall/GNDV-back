package com.gndv.payment.service;

import com.gndv.payment.constain.PaymentStatus;
import com.gndv.payment.domain.dto.LocalPayRequest;
import com.gndv.payment.domain.entity.LocalPayment;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.mapper.OrderMapper;
import com.gndv.payment.mapper.PaymentMapper;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;
    private final IamportClient iamportClient;

    @Override
    public LocalPayment createPayment(LocalPayRequest request) {
        Orders order = orderMapper.findOrderAndPaymentAndMember(request.getOrder_uid())
                .orElseThrow(() -> new IllegalArgumentException("Order not found for order_uid: " + request.getOrder_uid()));

        LocalPayment payment = LocalPayment.builder()
                .price(request.getPayment_price())
                .status(PaymentStatus.READY) // 초기 상태를 READY로 설정
                .payment_uid(request.getPayment_uid())
                .build();
        paymentMapper.insert(payment);

        // 주문에 결제 정보 업데이트
        Orders updatedOrder = Orders.builder()
                .order_id(order.getOrder_id())
                .order_uid(order.getOrder_uid())
                .buyer_id(order.getBuyer_id())
                .seller_id(order.getSeller_id())
                .price(order.getPrice())
                .item_name(order.getItem_name())
                .payment(payment) // 결제 정보 설정
                .buyer(order.getBuyer())
                .seller(order.getSeller())
                .build();
        orderMapper.update(updatedOrder);

        // Iamport API를 통해 결제 상태를 가져옵니다.
        try {
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPayment_uid());
            Payment iamportPayment = iamportResponse.getResponse();

            if (iamportPayment != null) {
                LocalPayment updatedPayment = payment.toBuilder()
                        .status(PaymentStatus.valueOf(iamportPayment.getStatus().toUpperCase()))
                        .build();
                paymentMapper.update(updatedPayment);
            } else {
                throw new IllegalArgumentException("Failed to fetch payment status from Iamport");
            }
        } catch (IamportResponseException | IOException e) {
            throw new IllegalArgumentException("Failed to fetch payment status from Iamport", e);
        }

        return payment;
    }

    @Override
    public LocalPayment findPaymentById(Long paymentId) {
        return paymentMapper.findById(paymentId);
    }

    @Override
    public void updatePayment(LocalPayment localPayment) {
        paymentMapper.update(localPayment);
    }

    @Override
    public void deletePayment(Long paymentId) {
        paymentMapper.delete(paymentId);
    }
}
