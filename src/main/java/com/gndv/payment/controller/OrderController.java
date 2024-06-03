package com.gndv.payment.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.MemberContext;
import com.gndv.payment.domain.dto.OrderCreateRequestDTO;
import com.gndv.payment.domain.dto.OrderResponseDTO;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.service.OrderService;
import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.product.domain.dto.request.ProductInsertWithMemberRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/order")
    public CustomResponse<OrderResponseDTO> order(@RequestParam(name = "order_uid") String orderUid) {
        Orders order = orderService.findOrderAndPaymentAndMember(orderUid);
        OrderResponseDTO response = OrderResponseDTO.builder()
                .order_uid(order.getOrder_uid())
                .item_name(order.getItem_name())
                .price(order.getPrice())
                .buyer_name(order.getBuyer().getNickname())
                .buyer_email(order.getBuyer().getEmail())
                .buyer_tel(order.getBuyer().getPhone())
                .buyer_postcode("123-456") // 임의의 우편번호 값
                .build();
        return CustomResponse.ok("주문을 성공적으로 조회했습니다.", response);
    }

    @GetMapping("/selectProduct")
    public CustomResponse<ProductInsertWithMemberRequest> selectProduct(@RequestParam("product_id") Long productId) {
        ProductInsertWithMemberRequest product = orderService.findProductInsertRequestById(productId);
        log.info("선택된 상품 = {}", product);
        return CustomResponse.ok("상품을 성공적으로 조회했습니다.", product);
    }

    @PostMapping("/order")
    public CustomResponse<OrderResponseDTO> createOrder(@RequestBody OrderCreateRequestDTO orderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long buyerId = ((MemberContext) authentication.getPrincipal()).getMemberDTO().getMember_id();

        log.info("받은 주문 파라미터: buyer_id={}, product_id={}", buyerId, orderRequest.getProduct_id());

        try {
            Orders order = orderService.createOrder(buyerId, orderRequest.getProduct_id());
            if (order != null) {
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
                return CustomResponse.ok("주문이 성공적으로 생성되었습니다.", response);
            } else {
                return CustomResponse.failure("주문 생성 실패");
            }
        } catch (Exception e) {
            log.error("주문 생성 실패", e);
            return CustomResponse.error("주문 생성 실패: " + e.getMessage());
        }
    }

    @GetMapping("/order/payment")
    public CustomResponse<OrderResponseDTO> payment(@RequestParam("order_uid") String orderUid) {
        try {
            Orders order = orderService.findOrderAndPaymentAndMember(orderUid);
            OrderResponseDTO response = OrderResponseDTO.builder()
                    .order_uid(order.getOrder_uid())
                    .item_name(order.getItem_name())
                    .price(order.getPrice())
                    .buyer_name(order.getBuyer().getNickname())
                    .buyer_email(order.getBuyer().getEmail())
                    .buyer_tel(order.getBuyer().getPhone())
                    .buyer_postcode("123-456")
                    .build();

            if (order.getPayment() != null) {
                response.setPaymentStatus(String.valueOf(order.getPayment().getStatus()));
                response.setPaymentPrice(order.getPayment().getPrice());
            }

            return CustomResponse.ok("주문 결제 정보를 성공적으로 조회했습니다.", response);
        } catch (IllegalArgumentException e) {
            return CustomResponse.failure("주문을 찾을 수 없습니다: " + e.getMessage());
        }
    }

    @PutMapping("/order/{order_uid}")
    public CustomResponse<OrderResponseDTO> updateOrder(@PathVariable("order_uid") String orderUid,
                                                        @RequestBody OrderCreateRequestDTO orderRequest) {
        try {
            Orders existingOrder = orderService.findOrderAndPaymentAndMember(orderUid);

            Orders updatedOrder = Orders.builder()
                    .order_id(existingOrder.getOrder_id())
                    .order_uid(existingOrder.getOrder_uid())
                    .buyer_id(existingOrder.getBuyer_id())
                    .seller_id(existingOrder.getSeller_id())
                    .price(orderRequest.getPrice())
                    .item_name(orderRequest.getItem_name())
                    .payment(existingOrder.getPayment())
                    .buyer(existingOrder.getBuyer())
                    .seller(existingOrder.getSeller())
                    .build();

            orderService.updateOrder(updatedOrder);

            OrderResponseDTO response = OrderResponseDTO.builder()
                    .order_uid(updatedOrder.getOrder_uid())
                    .item_name(updatedOrder.getItem_name())
                    .price(updatedOrder.getPrice())
                    .buyer_name(updatedOrder.getBuyer().getNickname())
                    .buyer_email(updatedOrder.getBuyer().getEmail())
                    .buyer_tel(updatedOrder.getBuyer().getPhone())
                    .buyer_postcode("123-456")
                    .build();

            return CustomResponse.ok("주문이 성공적으로 업데이트되었습니다.", response);
        } catch (Exception e) {
            log.error("주문 업데이트 실패", e);
            return CustomResponse.error("주문 업데이트 실패: " + e.getMessage());
        }
    }

    @DeleteMapping("/order/{order_uid}")
    public CustomResponse<String> deleteOrder(@PathVariable("order_uid") String orderUid) {
        try {
            orderService.deleteOrder(orderUid);
            return CustomResponse.ok("주문이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("주문 삭제 실패", e);
            return CustomResponse.error("주문 삭제 실패: " + e.getMessage());
        }
    }
    @GetMapping("/purchaseList")
    public CustomResponse<List<OrderResponseDTO>> getPurchaseList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long buyerId = ((MemberContext) authentication.getPrincipal()).getMemberDTO().getMember_id();

        List<Orders> orders = orderService.findOrdersByBuyerId(buyerId);
        List<OrderResponseDTO> response = orders.stream()
                .map(order -> OrderResponseDTO.builder()
                        .order_uid(order.getOrder_uid())
                        .item_name(order.getItem_name())
                        .price(order.getPrice())
                        .buyer_name(order.getBuyer().getNickname())
                        .buyer_email(order.getBuyer().getEmail())
                        .buyer_tel(order.getBuyer().getPhone())
                        .buyer_postcode("123-456") // 임의의 우편번호 값
                        .build())
                .collect(Collectors.toList());

        return CustomResponse.ok("구매 내역을 성공적으로 조회했습니다.", response);
    }
//    public CustomResponse<List<OrderResponseDTO>> getPurchaseList() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long buyerId = ((MemberContext) authentication.getPrincipal()).getMemberDTO().getMember_id();
//
//        List<Orders> orders = orderService.findOrdersByBuyerId(buyerId);
//        List<OrderResponseDTO> response = orders.stream()
//                .map(order -> {
//                    Review review = orderService.findReviewByOrderId(order.getOrder_id());
//                    return OrderResponseDTO.builder()
//                            .order_uid(order.getOrder_uid())
//                            .item_name(order.getItem_name())
//                            .price(order.getPrice())
//                            .buyer_name(order.getBuyer().getNickname())
//                            .buyer_email(order.getBuyer().getEmail())
//                            .buyer_tel(order.getBuyer().getPhone())
//                            .buyer_postcode("123-456") // 임의의 우편번호 값
//                            .reviewContent(review != null ? review.getReview_content() : null)
//                            .reviewRating(review != null ? review.getReview_rating() : null)
//                            .build();
//                })
//                .collect(Collectors.toList());
//
//        return CustomResponse.ok("구매 내역을 성공적으로 조회했습니다.", response);
//    }
}

//package com.gndv.payment.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.gndv.common.CustomResponse;
//import com.gndv.payment.domain.dto.OrderCreateRequestDTO;
//import com.gndv.payment.domain.entity.Orders;
//import com.gndv.payment.service.OrderService;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v2/order")
//public class OrderController {
//
//    private final OrderService orderService;
//    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
//    private final ObjectMapper objectMapper;
//
//    @PostMapping("/create")
//    public CustomResponse<String> createOrder(@RequestBody OrderCreateRequestDTO request) {
//        Long productId = request.getProduct_id();
//        Orders order = orderService.createOrder(productId);
//        try {
//            String orderJson = objectMapper.writeValueAsString(order);
//            return CustomResponse.success(orderJson, "Order created successfully");
//        } catch (JsonProcessingException e) {
//            logger.error("Error processing JSON", e);
//            return CustomResponse.failure("Error processing order creation");
//        }
//    }
//
//    @GetMapping("/{orderUid}")
//    public CustomResponse<String> findOrderAndPaymentAndMember(@PathVariable String orderUid) {
//        Orders order = orderService.findOrderAndPaymentAndMember(orderUid);
//        if (order != null) {
//            try {
//                String orderJson = objectMapper.writeValueAsString(order);
//                return CustomResponse.success(orderJson, "Order retrieved successfully");
//            } catch (JsonProcessingException e) {
//                logger.error("Error processing JSON", e);
//                return CustomResponse.failure("Error processing order retrieval");
//            }
//        } else {
//            return CustomResponse.failure("Order not found");
//        }
//    }
//
//    @PutMapping("/update")
//    public CustomResponse<String> updateOrder(@RequestBody Orders order) {
//        orderService.updateOrder(order);
//        return CustomResponse.success("Order updated successfully", "Order updated successfully");
//    }
//
//    @DeleteMapping("/delete/{orderUid}")
//    public CustomResponse<String> deleteOrder(@PathVariable String orderUid) {
//        orderService.deleteOrder(orderUid);
//        return CustomResponse.success("Order deleted successfully", "Order deleted successfully");
//    }
//}
