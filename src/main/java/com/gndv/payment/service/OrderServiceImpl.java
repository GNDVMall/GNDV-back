package com.gndv.payment.service;

import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.payment.constain.PaymentStatus;
import com.gndv.payment.domain.dto.OrderCreateRequestDTO;
import com.gndv.payment.domain.entity.LocalPayment;
import com.gndv.payment.domain.entity.OrderList;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.mapper.OrderMapper;
import com.gndv.product.domain.dto.request.ProductInsertWithMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberMapper memberMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public Orders createOrder(Long buyerId, OrderCreateRequestDTO request) {
        Long productId = request.getProduct_id(); // Request에서 product_id를 가져옴

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
                .price(request.getPrice())
                .status(PaymentStatus.READY)
                .payment_uid(UUID.randomUUID().toString())
                .member_id(buyerId)
                .build();
        orderMapper.savePayment(payment);

        // Orders 생성
        Orders order = Orders.builder()
                .buyer_id(buyerId)
                .seller_id(sellerId)
                .price(request.getPrice())
                .item_name(request.getItem_name())
                .order_uid(UUID.randomUUID().toString())
                .payment_id(payment.getPayment_id())
                .product_id(productId)
                .buyer(buyer)  // 추가된 필드 설정
                .seller(seller)  // 추가된 필드 설정
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

        // Product 상태 업데이트 (트리거 대신 직접 업데이트)
        orderMapper.updateProductStatusToSoldOut(productId);

        return order;
    }


    @Override
    public Optional<Orders> findOrderAndPaymentAndMember(String orderUid) {
        return orderMapper.findOrderAndPaymentAndMember(orderUid);
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
        Orders order = findOrderAndPaymentAndMember(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with UID: " + orderUid));
        orderMapper.delete(order.getOrder_id());
    }

    @Override
    public List<Orders> findOrdersBySellerId(Long sellerId) {
        return orderMapper.findOrdersBySellerId(sellerId);
    }
    @Override
    public void updateProductStatusToSoldOut(Long productId) {
        orderMapper.updateProductStatusToSoldOut(productId);
    }
}
