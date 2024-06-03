package com.gndv.payment.controller;

import com.gndv.common.CustomResponse;
import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @GetMapping("/order/selectProduct")
    public CustomResponse<ProductInsertRequest> selectProduct(@RequestParam("product_id") Long productId) {
        ProductInsertRequest product = orderService.findProductInsertRequestById(productId);
        log.info("선택된 상품 = {}", product);
        return CustomResponse.ok("상품을 성공적으로 조회했습니다.", product);
    }

    @PostMapping("/order/create")
    public CustomResponse<Orders> createOrder(@RequestParam("buyer_id") Long buyerId, @RequestParam("product_id") Long productId) {
        Orders order = orderService.createOrder(buyerId, productId);
        log.info("생성된 주문 = {}", order);
        return CustomResponse.ok("주문을 성공적으로 생성했습니다.", order);
    }
}
