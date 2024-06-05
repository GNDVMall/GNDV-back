package com.gndv.payment.controller;

import com.gndv.common.CustomResponse;
import com.gndv.payment.constain.PaymentStatus;
import com.gndv.payment.domain.dto.LocalPayRequest;
import com.gndv.payment.domain.entity.LocalPayment;
import com.gndv.payment.service.PaymentService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/payment")
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
        try {
            LocalPayment updatedPayment = LocalPayment.builder()
                    .payment_id(payment_id)
                    .price(request.getPayment_price())
                    .status(PaymentStatus.valueOf(request.getStatus().toUpperCase())) // 상태 업데이트
                    .payment_uid(request.getPayment_uid())
                    .build();
            paymentService.updatePayment(updatedPayment);
            return CustomResponse.ok("결제 정보가 성공적으로 업데이트되었습니다.", updatedPayment);
        } catch (Exception e) {
            log.error("결제 정보 업데이트 실패", e);
            return CustomResponse.error("결제 정보 업데이트 실패: " + e.getMessage());
        }
    }

    @DeleteMapping("/{payment_id}")
    public CustomResponse<Void> deletePayment(@PathVariable Long payment_id) {
        try {
            paymentService.deletePayment(payment_id);
            return CustomResponse.ok("결제 정보가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("결제 정보 삭제 실패", e);
            return CustomResponse.error("결제 정보 삭제 실패: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<CustomResponse<IamportResponse<Payment>>> validationPayment(@RequestBody LocalPayRequest request) {

        try {
            if (request.getImp_uid() == null) {
                throw new IllegalArgumentException("imp_uid must not be null");
            }
            IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);
            log.info("결제 응답={}", iamportResponse.getResponse().toString());
            return ResponseEntity.ok(CustomResponse.ok("결제 검증 성공", iamportResponse));
        } catch (Exception e) {
            log.error("결제 검증 실패", e);
            System.out.println(request);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomResponse.error("결제 검증 실패: " + e.getMessage()));
        }
    }
}
