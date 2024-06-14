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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    @GetMapping("/purchaseList")
    public CustomResponse<List<OrderResponseDTO>> getPurchaseList(@AuthenticationPrincipal MemberContext memberContext) {
        if (memberContext == null) {
            return CustomResponse.failure("Not an authenticated user");
        }
        Long memberId = memberContext.getMemberDTO().getMember_id();
        List<Orders> orders = orderService.findOrdersByBuyerId(memberId);
        List<OrderResponseDTO> purchaseList = orders.stream().map(order -> {
            OrderResponseDTO.OrderResponseDTOBuilder builder = OrderResponseDTO.builder()
                    .order_uid(order.getOrder_uid())
                    .item_name(order.getItem_name())
                    .price(order.getPrice())
                    .product_id(order.getProduct_id() != null ? order.getProduct_id() : 0) // Default value or handle accordingly
                    .buyer_name(order.getBuyer().getNickname())
                    .buyer_email(order.getBuyer().getEmail())
                    .buyer_tel(order.getBuyer().getPhone())
                    .buyer_postcode("123-456"); // Assuming this is a static value as in your original code

            if (order.getPayment() != null && order.getPayment().getStatus() != null) {
                builder.payment_status(order.getPayment().getStatus().toString());
            } else {
                builder.payment_status("N/A"); // Or any default value you prefer
            }

            return builder.build();
        }).collect(Collectors.toList());
        return CustomResponse.ok("Purchase list fetched successfully", purchaseList);
    }

    @GetMapping("/salesList")
    public CustomResponse<List<OrderResponseDTO>> getSalesList(@AuthenticationPrincipal MemberContext memberContext) {
        if (memberContext == null) {
            return CustomResponse.failure("Not an authenticated user");
        }
        Long memberId = memberContext.getMemberDTO().getMember_id();
        List<Orders> orders = orderService.findOrdersBySellerId(memberId);
        List<OrderResponseDTO> salesList = orders.stream().map(order -> OrderResponseDTO.builder()
                .order_uid(order.getOrder_uid())
                .item_name(order.getItem_name())
                .price(order.getPrice())
                .buyer_name(order.getBuyer() != null ? order.getBuyer().getNickname() : "N/A")
                .buyer_email(order.getBuyer() != null ? order.getBuyer().getEmail() : "N/A")
                .buyer_tel(order.getBuyer() != null ? order.getBuyer().getPhone() : "N/A")
                .buyer_postcode("123-456")
                .review_id(order.getReview_id())
                .product_id(order.getProduct_id())  // Ensure product_id is included
                .build()).collect(Collectors.toList());
        return CustomResponse.ok("Sales list fetched successfully", salesList);
    }


    @GetMapping("/product/{orderUid}")
    public CustomResponse<Map<String, Long>> getProductIdByOrderUid(@PathVariable String orderUid) {
        Orders order = orderService.findOrderAndPaymentAndMember(orderUid)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Map<String, Long> response = new HashMap<>();
        response.put("product_id", order.getProduct_id());
        return CustomResponse.ok("Product ID fetched successfully", response);
    }
}
