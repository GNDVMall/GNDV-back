package com.gndv.payment.service;

import com.gndv.payment.domain.entity.Orders;
import com.gndv.product.domain.dto.request.ProductInsertRequest;

public interface OrderService {
    Orders createOrder(Long buyerId, Long productId);
    Orders findOrderAndPaymentAndMember(String orderUid);
    void updateOrder(Orders order);
    void deleteOrder(String orderUid);

    // 새로운 메서드 추가
    ProductInsertRequest findProductInsertRequestById(Long productId);
}
