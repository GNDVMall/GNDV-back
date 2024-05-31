package com.gndv.payment.controller;

import com.gndv.common.CustomResponse;
import com.gndv.payment.domain.dto.OrderCreateRequestDTO;
import com.gndv.payment.domain.dto.OrderResponseDTO;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.service.OrderService;
import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.product.service.ProductService;
import com.gndv.member.domain.dto.MemberContext;
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
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;

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

    @GetMapping("/order/selectProduct")
    public CustomResponse<ProductInsertRequest> selectProduct(@RequestParam("product_id") Long productId) {
        ProductInsertRequest product = productService.findProductInsertRequestById(productId);
        log.info("선택된 상품 = {}", product);
        return CustomResponse.ok("상품을 성공적으로 조회했습니다.", product);
    }

    @PostMapping("/order")
    public CustomResponse<OrderResponseDTO> createOrder(@RequestBody OrderCreateRequestDTO orderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberContext memberContext = (MemberContext) authentication.getPrincipal();
        Long buyerId = memberContext.getMemberDTO().getMember_id();

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
                response.setPaymentStatus(order.getPayment().getStatus());
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
}
