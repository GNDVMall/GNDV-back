package com.gndv.payment.service;

import com.gndv.member.domain.entity.Member;
import com.gndv.payment.domain.entity.Orders;

public interface OrderService {
    Orders autoOrder(Member member);

    Orders createOrder(Long buyerId, Long sellerId, Long price, String itemName);

    Orders findOrderAndPaymentAndMember(String orderUid);
}
