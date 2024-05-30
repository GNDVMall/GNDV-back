package com.gndv.payment.service;

import com.gndv.member.domain.entity.Member;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.mapper.OrderMapper;
import com.siot.IamportRestClient.IamportClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper ordersMapper;
    private final IamportClient iamportClient;

    @Override
    public Orders autoOrder(Member member) {
        // 자동 주문 로직 구현 (필요한 경우)
        return null;
    }

    @Override
    public Orders createOrder(Long buyerId, Long sellerId, Long price, String itemName) {
        Orders order = Orders.builder()
                .buyer_id(buyerId)
                .seller_id(sellerId)
                .price(price)
                .item_name(itemName)
                .order_uid(UUID.randomUUID().toString())
                .build();
        ordersMapper.save(order);
        return order;
    }

    @Override
    public Orders findOrderAndPaymentAndMember(String orderUid) {
        return ordersMapper.findOrderAndPaymentAndMember(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("Order not found for order_uid: " + orderUid));
    }
}
