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
        String impUid = request.getImp_uid();

        try {
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(impUid);
            Payment iamportPayment = iamportResponse.getResponse();

            if (iamportPayment != null) {
                LocalPayment payment = LocalPayment.builder()
                        .price((long) iamportPayment.getAmount().intValue())
                        .status(PaymentStatus.valueOf(iamportPayment.getStatus().toUpperCase()))
                        .payment_uid(impUid)
                        .build();
                paymentMapper.insert(payment);

                Orders order = orderMapper.findOrderAndPaymentAndMember(request.getOrder_uid())
                        .orElseThrow(() -> new IllegalArgumentException("Order not found for order_uid: " + request.getOrder_uid()));

                Orders updatedOrder = Orders.builder()
                        .order_id(order.getOrder_id())
                        .order_uid(order.getOrder_uid())
                        .buyer_id(order.getBuyer_id())
                        .seller_id(order.getSeller_id())
                        .price(order.getPrice())
                        .item_name(order.getItem_name())
                        .payment(payment)
                        .buyer(order.getBuyer())
                        .seller(order.getSeller())
                        .build();
                orderMapper.update(updatedOrder);

                return payment;
            } else {
                throw new IllegalArgumentException("Failed to fetch payment status from Iamport");
            }
        } catch (IamportResponseException | IOException e) {
            throw new IllegalArgumentException("Failed to fetch payment status from Iamport", e);
        }
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
