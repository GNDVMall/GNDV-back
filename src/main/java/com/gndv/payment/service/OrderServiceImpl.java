package com.gndv.payment.service;

import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.payment.domain.dto.LocalPayRequest;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.mapper.OrderMapper;
import com.gndv.product.domain.dto.request.ProductInsertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final MemberMapper memberMapper; // Assuming you have a MemberMapper to get member information
    private final PaymentService paymentService; // Assuming you have a PaymentService to handle payments

    @Override
    public Orders createOrder(Long buyerId, Long productId) {
        // 구매자 정보 조회
        Member buyer = memberMapper.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("Buyer not found"));

        // 상품 정보 조회
        ProductInsertRequest product = orderMapper.findProductInsertRequestById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        // 판매자 정보 조회
        Long sellerId = product.getMember_id();
        Member seller = memberMapper.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        // 주문 생성
        Orders order = Orders.builder()
                .buyer_id(buyerId)
                .seller_id(sellerId)
                .price(product.getPrice())
                .item_name(product.getTitle())
                .order_uid(UUID.randomUUID().toString())
                .buyer(buyer)
                .seller(seller)
                .build();
        orderMapper.save(order);

        // 결제 생성
        LocalPayRequest paymentRequest = LocalPayRequest.builder()
                .order_uid(order.getOrder_uid())
                .item_id(productId)
                .item_name(product.getTitle())
                .username(buyer.getNickname())
                .payment_price(product.getPrice())
                .email(buyer.getEmail())
                .address("buyer address") // 실제 주소로 대체
                .payment_uid(UUID.randomUUID().toString())
                .build();

        paymentService.createPayment(paymentRequest);

        return order;
    }

    @Override
    public Orders findOrderAndPaymentAndMember(String orderUid) {
        return orderMapper.findOrderAndPaymentAndMember(orderUid).orElse(null);
    }

    @Override
    public void updateOrder(Orders order) {
        orderMapper.update(order);
    }

    @Override
    public void deleteOrder(String orderUid) {
        Orders order = orderMapper.findOrderAndPaymentAndMember(orderUid).orElse(null);
        if (order != null) {
            orderMapper.delete(order.getOrder_id());
        }
    }

    @Override
    public ProductInsertRequest findProductInsertRequestById(Long productId) {
        return orderMapper.findProductInsertRequestById(productId);
    }
}
