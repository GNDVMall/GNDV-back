
package com.gndv.payment.service;

import com.gndv.payment.domain.entity.Orders;
import com.gndv.product.domain.dto.request.ProductInsertWithMemberRequest;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface OrderService {
    Orders createOrder(Long buyerId, Long productId);
    Orders findOrderAndPaymentAndMember(String orderUid);
    void updateOrder(Orders order);
    void deleteOrder(String orderUid);
    ProductInsertWithMemberRequest findProductInsertRequestById(Long productId); // 추가된 메서드

    List<Orders> findOrdersByBuyerId(Long buyerId);
}

