package com.gndv.payment.controller;

import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.service.OrderService;
import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
@SessionAttributes({"selectedSellerId", "selectedBuyerId", "selectedProduct"})
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;

    @ModelAttribute("selectedProduct")
    public ProductInsertRequest setUpProduct() {
        return new ProductInsertRequest(); // 기본 생성자를 사용하여 초기화
    }

    @GetMapping("/order")
    public ResponseEntity<?> order(@RequestParam(name = "message", required = false) String message,
                                   @RequestParam(name = "order_uid", required = false) String orderUid) {

        Map<String, Object> response = new HashMap<>();
        response.put("message", message != null ? message : "");
        response.put("orderUid", orderUid != null ? orderUid : "");

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/order/selectProduct")
    public ResponseEntity<?> selectProduct(@RequestParam("product_id") Long productId, Model model) {
        ProductInsertRequest product = productService.findProductInsertRequestById(productId);
        model.addAttribute("selectedProduct", product);
        log.info("selectedProduct = {}", product);
        log.info("productId = {}", productId);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@SessionAttribute("selectedBuyerId") Long buyerId,
                                         @SessionAttribute("selectedSellerId") Long sellerId,
                                         @SessionAttribute("selectedProduct") ProductInsertRequest product) {

        // Log the incoming request parameters
        log.info("Received order request with parameters: buyer_id={}, seller_id={}, product_id={}, price={}, item_name={}",
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
            Orders order = orderService.findOrderAndPaymentAndMember(orderUid);
            return ResponseEntity.ok().body(Map.of(
                    "order_uid", order.getOrder_uid(),
                    "item_name", order.getItem_name(),
                    "price", order.getPrice(),
                    "buyer_name", order.getBuyer().getNickname(),
                    "buyer_email", order.getBuyer().getEmail(),
                    "buyer_tel", order.getBuyer().getPhone(),
                    "buyer_postcode", "123-456" // 임의의 우편번호 값
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
