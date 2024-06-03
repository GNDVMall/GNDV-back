package com.gndv.payment.controller;

import com.gndv.common.CustomResponse;
import com.gndv.payment.constain.PaymentStatus;
import com.gndv.payment.domain.dto.LocalPayRequest;
import com.gndv.payment.domain.entity.LocalPayment;
import com.gndv.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public CustomResponse<LocalPayment> createPayment(@RequestBody LocalPayRequest request) {
        try {
            LocalPayment payment = paymentService.createPayment(request);
            return CustomResponse.ok("결제가 성공적으로 생성되었습니다.", payment);
        } catch (Exception e) {
            log.error("결제 생성 실패", e);
            return CustomResponse.error("결제 생성 실패: " + e.getMessage());
        }
    }

    @GetMapping("/{payment_id}")
    public CustomResponse<LocalPayment> getPayment(@PathVariable Long payment_id) {
        LocalPayment payment = paymentService.findPaymentById(payment_id);
        return CustomResponse.ok("결제 정보를 성공적으로 조회했습니다.", payment);
    }

    @PutMapping("/{payment_id}")
    public CustomResponse<LocalPayment> updatePayment(@PathVariable Long payment_id, @RequestBody LocalPayRequest request) {
        LocalPayment updatedPayment = LocalPayment.builder()
                .payment_id(payment_id)
                .price(request.getPayment_price())
                .status(PaymentStatus.valueOf(request.getStatus().toUpperCase())) // 상태 업데이트
                .payment_uid(request.getPayment_uid())
                .build();
        paymentService.updatePayment(updatedPayment);
        return CustomResponse.ok("결제 정보가 성공적으로 업데이트되었습니다.", updatedPayment);
    }

    @DeleteMapping("/{payment_id}")
    public CustomResponse<Void> deletePayment(@PathVariable Long payment_id) {
        paymentService.deletePayment(payment_id);
        return CustomResponse.ok("결제 정보가 성공적으로 삭제되었습니다.");
    }
}
