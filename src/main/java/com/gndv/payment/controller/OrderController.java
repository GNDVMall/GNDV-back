package com.gndv.payment.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.MemberContext;
import com.gndv.payment.domain.dto.LocalPayRequest;
import com.gndv.payment.domain.dto.OrderCreateRequestDTO;
import com.gndv.payment.domain.dto.OrderResponseDTO;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.service.OrderService;
import com.gndv.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Order API", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 생성 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/order")
    public CustomResponse<OrderResponseDTO> createOrder(
            @Parameter(description = "주문 생성 요청 객체", required = true) @RequestBody OrderCreateRequestDTO orderRequest) {
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

    @Operation(summary = "결제 콜백", description = "결제 상태를 업데이트합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제 상태 업데이트 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/payment/callback")
    public CustomResponse<String> paymentCallback(
            @Parameter(description = "결제 요청 객체", required = true) @RequestBody LocalPayRequest request) {
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

    @Operation(summary = "구매 목록 조회", description = "사용자의 구매 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구매 목록 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/purchaseList")
    public CustomResponse<List<OrderResponseDTO>> getPurchaseList(
            @Parameter(description = "사용자 인증 정보", required = true) @AuthenticationPrincipal MemberContext memberContext) {
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
                    .product_id(order.getProduct_id() != null ? order.getProduct_id() : 0)
                    .buyer_name(order.getBuyer().getNickname())
                    .buyer_email(order.getBuyer().getEmail())
                    .buyer_tel(order.getBuyer().getPhone())
                    .buyer_postcode("123-456");

            if (order.getPayment() != null && order.getPayment().getStatus() != null) {
                builder.payment_status(order.getPayment().getStatus().toString());
            } else {
                builder.payment_status("N/A");
            }

            return builder.build();
        }).collect(Collectors.toList());
        return CustomResponse.ok("Purchase list fetched successfully", purchaseList);
    }

    @Operation(summary = "판매 목록 조회", description = "사용자의 판매 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "판매 목록 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/salesList")
    public CustomResponse<List<OrderResponseDTO>> getSalesList(
            @Parameter(description = "사용자 인증 정보", required = true) @AuthenticationPrincipal MemberContext memberContext) {
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
                .product_id(order.getProduct_id())
                .build()).collect(Collectors.toList());
        return CustomResponse.ok("Sales list fetched successfully", salesList);
    }

    @Operation(summary = "상품 ID 조회", description = "주문 UID를 통해 상품 ID를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 ID 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/product/{orderUid}")
    public CustomResponse<Map<String, Long>> getProductIdByOrderUid(
            @Parameter(description = "주문 UID", required = true) @PathVariable String orderUid) {
        Orders order = orderService.findOrderAndPaymentAndMember(orderUid)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Map<String, Long> response = new HashMap<>();
        response.put("product_id", order.getProduct_id());
        return CustomResponse.ok("Product ID fetched successfully", response);
    }
}
