package com.gndv.payment.service;

import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.payment.constain.PaymentStatus;
import com.gndv.payment.domain.entity.LocalPayment;
import com.gndv.payment.domain.entity.OrderList;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.mapper.OrderMapper;
import com.gndv.product.domain.dto.request.ProductInsertWithMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberMapper memberMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public Orders createOrder(Long buyerId, Long productId) {
        Member buyer = memberMapper.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("Buyer not found"));

        ProductInsertWithMemberRequest product = orderMapper.findProductInsertRequestById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found for productId: " + productId);
        }

        Long sellerId = product.getMember_id();

        Member seller = memberMapper.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        // LocalPayment 생성
        LocalPayment payment = LocalPayment.builder()
                .price(Long.valueOf(product.getPrice()))
                .status(PaymentStatus.READY)
                .payment_uid(UUID.randomUUID().toString())
                .member_id(buyerId)
                .build();
        orderMapper.savePayment(payment);

        // Orders 생성
        Orders order = Orders.builder()
                .buyer_id(buyerId)
                .seller_id(sellerId)
                .price(Long.valueOf(product.getPrice()))
                .item_name(product.getTitle())
                .order_uid(UUID.randomUUID().toString())
                .payment_id(payment.getPayment_id())
                .buyer(buyer)
                .seller(seller)
                .build();
        orderMapper.save(order);

        // OrderList 생성
        OrderList orderList = OrderList.builder()
                .order_id(order.getOrder_id())
                .payment_id(payment.getPayment_id())
                .seller_id(sellerId)
                .buyer_id(buyerId)
                .build();
        orderMapper.saveOrderList(orderList);

        return order;
    }

    @Override
    public Orders findOrderAndPaymentAndMember(String orderUid) {
        return orderMapper.findOrderAndPaymentAndMember(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("Order not found for orderUid: " + orderUid));
    }

    @Override
    public List<Orders> findOrdersByBuyerId(Long buyerId) {
        return orderMapper.findOrdersByBuyerId(buyerId);
    }

    @Override
    @Transactional
    public void updateOrder(Orders order) {
        orderMapper.update(order);
    }

    @Override
    @Transactional
    public void deleteOrder(String orderUid) {
        Orders order = findOrderAndPaymentAndMember(orderUid);
        orderMapper.delete(order.getOrder_id());
    }
    @Override
    public List<Orders> findOrdersBySellerId(Long sellerId) {
        return orderMapper.findOrdersBySellerId(sellerId);
    }
}
