package com.gndv.payment.service;

import com.gndv.payment.domain.entity.Orders;

public interface OrderService {
    Orders createOrder(Long buyerId, Long productId);
    Orders findOrderAndPaymentAndMember(String orderUid);
    void updateOrder(Orders order);
    void deleteOrder(String orderUid);
}
