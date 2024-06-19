package com.gndv.payment.service;

import com.gndv.payment.domain.dto.OrderCreateRequestDTO;
import com.gndv.payment.domain.entity.Orders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Orders createOrder(Long buyerId, OrderCreateRequestDTO request);

    Optional<Orders> findOrderAndPaymentAndMember(String orderUid);

    List<Orders> findOrdersByBuyerId(Long buyerId);

    void updateOrder(Orders order);

    void deleteOrder(String orderUid);

    List<Orders> findOrdersBySellerId(Long sellerId);
    void updateProductStatusToSoldOut(Long productId);
}