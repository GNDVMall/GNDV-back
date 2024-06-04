package com.gndv.payment.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.MemberContext;
import com.gndv.member.domain.dto.MemberDTO;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.payment.domain.dto.OrderCreateRequestDTO;
import com.gndv.payment.domain.dto.OrderResponseDTO;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final MemberMapper memberMapper;
    private final ModelMapper modelMapper; // 추가

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

    @PostMapping("/order")
    public CustomResponse<OrderResponseDTO> createOrder(@RequestBody OrderCreateRequestDTO orderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        log.info("Principal class: {}", principal.getClass().getName()); // 추가된 로그

        if (principal instanceof MemberContext) {
            Long buyerId = ((MemberContext) principal).getMemberDTO().getMember_id();
            log.info("Received order request: buyer_id={}, product_id={}", buyerId, orderRequest.getProduct_id());

            try {
                Orders order = orderService.createOrder(buyerId, orderRequest.getProduct_id());
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
            } catch (Exception e) {
                log.error("Order creation failed", e);
                return CustomResponse.error("Order creation failed: " + e.getMessage());
            }
        } else {
            log.error("Authentication failed: Principal is not MemberContext. Actual principal class: {}", principal.getClass().getName());
            return CustomResponse.failure("Not an authenticated user");
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
}
