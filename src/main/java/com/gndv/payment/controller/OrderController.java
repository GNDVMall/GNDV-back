package com.gndv.payment.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.MemberContext;
import com.gndv.payment.domain.dto.LocalPayRequest;
import com.gndv.payment.domain.dto.OrderCreateRequestDTO;
import com.gndv.payment.domain.dto.OrderResponseDTO;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.service.OrderService;
import com.gndv.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    @PostMapping("/order")
    public CustomResponse<OrderResponseDTO> createOrder(@RequestBody OrderCreateRequestDTO orderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        log.info("Principal class: {}", principal.getClass().getName());

        if (principal instanceof MemberContext) {
            Long buyerId = ((MemberContext) principal).getMemberDTO().getMember_id();
            log.info("Received order request: buyer_id={}, product_id={}", buyerId, orderRequest.getProduct_id());

            try {
                Orders order = orderService.createOrder(buyerId, orderRequest);
                if (order != null) {
                    log.info("Order created successfully: {}", order);
                    String encodedOrderUid = URLEncoder.encode(order.getOrder_uid(), StandardCharsets.UTF_8);
                    OrderResponseDTO response = OrderResponseDTO.builder()
                            .order_uid(encodedOrderUid)
                            .item_name(order.getItem_name())
                            .price(order.getPrice())
                            .buyer_name(order.getBuyer().getNickname())
                            .buyer_email(order.getBuyer().getEmail())
                            .buyer_tel(order.getBuyer().getPhone())
                            .buyer_postcode("123-456")
                            .build();
                    return CustomResponse.ok("Order successfully created.", response);
                } else {
                    log.error("Order creation failed: Order is null");
                    return CustomResponse.failure("Order creation failed");
                }
            } catch (IllegalArgumentException e) {
                log.error("Order creation failed", e);
                return CustomResponse.error("Order creation failed: " + e.getMessage());
            } catch (Exception e) {
                log.error("Order creation failed", e);
                return CustomResponse.error("Order creation failed: " + e.getMessage());
            }
        } else {
            log.error("Authentication failed: Principal is not MemberContext. Actual principal class: {}", principal.getClass().getName());
            return CustomResponse.failure("Not an authenticated user");
        }
    }

    @PostMapping("/payment/callback")
    public CustomResponse<String> paymentCallback(@RequestBody LocalPayRequest request) {
        try {
            if ("PAID".equals(request.getStatus().toUpperCase())) {
                paymentService.updateProductStatusToSoldOutIfPaid(request.getOrder_uid());
            }
            return CustomResponse.ok("Payment status updated successfully.");
        } catch (Exception e) {
            log.error("Failed to update payment status", e);
            return CustomResponse.error("Failed to update payment status: " + e.getMessage());
        }
    }
}
