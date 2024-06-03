package com.gndv.payment.service;

import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.mapper.OrderMapper;
import com.gndv.product.domain.dto.request.ProductInsertWithMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper ordersMapper;
    private final MemberMapper memberMapper;

    @Override
    public Orders createOrder(Long buyerId, Long productId) {
        Member buyer = memberMapper.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("Buyer not found"));

        ProductInsertWithMemberRequest product = findProductInsertRequestById(productId);
        Long sellerId = product.getMember_id();

        Member seller = memberMapper.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        Orders order = Orders.builder()
                .buyer_id(buyerId)
                .seller_id(sellerId)
                .price(Long.valueOf(product.getPrice()))
                .item_name(product.getTitle())
                .order_uid(UUID.randomUUID().toString())
                .buyer(buyer)
                .seller(seller)
                .build();
        ordersMapper.save(order);
        return order;
    }

    @Override
    public Orders findOrderAndPaymentAndMember(String orderUid) {
        return ordersMapper.findOrderAndPaymentAndMember(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("Order not found for order_uid: " + orderUid));
    }

    @Override
    public void updateOrder(Orders order) {
        ordersMapper.update(order);
    }

    @Override
    public void deleteOrder(String orderUid) {
        Orders order = ordersMapper.findOrderAndPaymentAndMember(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("Order not found for order_uid: " + orderUid));
        ordersMapper.delete(order.getOrder_id());
    }

    @Override
    public ProductInsertWithMemberRequest findProductInsertRequestById(Long productId) {
        return ordersMapper.findProductInsertRequestById(productId);
    }

    @Override
    public List<Orders> findOrdersByBuyerId(Long buyerId) {
        return ordersMapper.findOrdersByBuyerId(buyerId);
    }

//    public Review findReviewByOrderId(Long orderId) {
//        return orderMapper.findReviewByOrderId(orderId)
//    }
}


