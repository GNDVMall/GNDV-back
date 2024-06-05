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

    private final OrderMapper ordersMapper;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public Orders createOrder(Long buyerId, Long productId) {
        Member buyer = memberMapper.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("Buyer not found"));

        ProductInsertWithMemberRequest product = findProductInsertRequestById(productId);
        Long sellerId = product.getMember_id();

        Member seller = memberMapper.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        // Gangnum_Payment 생성
        LocalPayment payment = LocalPayment.builder()
                .price(Long.valueOf(product.getPrice()))
                .status(PaymentStatus.READY)  // 수정된 부분
                .payment_uid(UUID.randomUUID().toString())
                .member_id(buyerId)
                .build();
        ordersMapper.savePayment(payment);

        // Orders 생성
        Orders order = Orders.builder()
                .buyer_id(buyerId)
                .seller_id(sellerId)
                .price(Long.valueOf(product.getPrice()))
                .item_name(product.getTitle())
                .order_uid(UUID.randomUUID().toString())
                .payment_id(payment.getPayment_id())  // payment_id 설정
                .buyer(buyer)
                .seller(seller)
                .build();
        ordersMapper.save(order);

        // OrderList 생성
        OrderList orderList = OrderList.builder()
                .order_id(order.getOrder_id())
                .payment_id(payment.getPayment_id())
                .seller_id(sellerId)
                .buyer_id(buyerId)
                .build();
        ordersMapper.saveOrderList(orderList);

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
}
