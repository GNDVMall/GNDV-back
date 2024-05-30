package com.gndv.payment.controller;

import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.service.OrderService;
import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.product.service.ProductService;
import com.gndv.member.domain.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;

    @GetMapping("/order")
    public ResponseEntity<?> order(@RequestParam(name = "message", required = false) String message,
                                   @RequestParam(name = "order_uid", required = false) String orderUid) {

        Map<String, Object> response = new HashMap<>();
        response.put("message", message != null ? message : "");
        response.put("orderUid", orderUid != null ? orderUid : "");

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/order/selectProduct")
    public ResponseEntity<?> selectProduct(@RequestParam("product_id") Long productId) {
        ProductInsertRequest product = productService.findProductInsertRequestById(productId);
        log.info("선택된 상품 = {}", product);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestParam("product_id") Long productId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberContext memberContext = (MemberContext) authentication.getPrincipal();
        Long buyerId = memberContext.getMemberDTO().getMember_id();

        ProductInsertRequest product = productService.findProductInsertRequestById(productId);
        Long sellerId = product.getMember_id();

        log.info("받은 주문 파라미터: buyer_id={}, seller_id={}, product_id={}, price={}, item_name={}",
                buyerId, sellerId, product.getItem_id(), product.getPrice(), product.getTitle());

        try {
            Orders order = orderService.createOrder(buyerId, sellerId, product.getPrice(), product.getTitle());

            if (order != null) {
                String encodedOrderUid = URLEncoder.encode(order.getOrder_uid(), StandardCharsets.UTF_8);
                return ResponseEntity.ok().body(Map.of("orderUid", encodedOrderUid));
            } else {
                String message = "주문 실패";
                String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
                return ResponseEntity.badRequest().body(Map.of("message", encodedMessage));
            }
        } catch (Exception e) {
            log.error("Order creation failed", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/order/payment")
    public ResponseEntity<?> payment(@RequestParam("order_uid") String orderUid) {
        try {
            Optional<Orders> optionalOrder = Optional.ofNullable(orderService.findOrderAndPaymentAndMember(orderUid));

            if (optionalOrder.isPresent()) {
                Orders order = optionalOrder.get();
                Map<String, Object> response = new HashMap<>();
                response.put("order_uid", order.getOrder_uid());
                response.put("item_name", order.getItem_name());
                response.put("price", order.getPrice());
                response.put("buyer_name", order.getBuyer().getNickname());
                response.put("buyer_email", order.getBuyer().getEmail());
                response.put("buyer_tel", order.getBuyer().getPhone());
                response.put("buyer_postcode", "123-456"); // 임의의 우편번호 값

                return ResponseEntity.ok().body(response);
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Order not found"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
