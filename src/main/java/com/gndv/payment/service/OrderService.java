package com.gndv.payment.service;

import com.gndv.payment.domain.entity.Orders;

import java.util.List;

public interface OrderService {
    Orders createOrder(Long buyerId, Long productId);

    Orders findOrderAndPaymentAndMember(String orderUid);

    List<Orders> findOrdersByBuyerId(Long buyerId);

    void updateOrder(Orders order);

    void deleteOrder(String orderUid);

    List<Orders> findOrdersBySellerId(Long sellerId);
}