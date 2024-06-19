package com.gndv.payment.controller;

import com.gndv.common.CustomResponse;
import com.gndv.payment.domain.dto.LocalPayRequest;
import com.gndv.payment.domain.entity.LocalPayment;
import com.gndv.payment.service.PaymentService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/payment")
@Tag(name = "Payment API", description = "결제 관련 API")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 생성", description = "새로운 결제를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제 생성 성공", content = @Content(schema = @Schema(implementation = LocalPayment.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public CustomResponse<LocalPayment> createPayment(
            @Parameter(description = "결제 요청 객체", required = true) @RequestBody LocalPayRequest request) {
        try {
            LocalPayment payment = paymentService.createPayment(request);
            return CustomResponse.ok("결제가 성공적으로 생성되었습니다.", payment);
        } catch (Exception e) {
            log.error("결제 생성 실패", e);
            return CustomResponse.error("결제 생성 실패: " + e.getMessage());
        }
    }

    @Operation(summary = "결제 정보 조회", description = "결제 ID를 통해 결제 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제 정보 조회 성공", content = @Content(schema = @Schema(implementation = LocalPayment.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "결제를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{payment_id}")
    public CustomResponse<LocalPayment> getPayment(
            @Parameter(description = "결제 ID", required = true) @PathVariable Long payment_id) {
        LocalPayment payment = paymentService.findPaymentById(payment_id);
        return CustomResponse.ok("결제 정보를 성공적으로 조회했습니다.", payment);
    }

    @Operation(summary = "결제 검증", description = "결제 정보를 검증합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제 검증 성공", content = @Content(schema = @Schema(implementation = IamportResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/validate")
    public ResponseEntity<CustomResponse<IamportResponse<Payment>>> validationPayment(
            @Parameter(description = "결제 요청 객체", required = true) @RequestBody LocalPayRequest request) {

        try {
            if (request.getImp_uid() == null) {
                throw new IllegalArgumentException("imp_uid must not be null");
            }
            IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);
            log.info("결제 응답={}", iamportResponse.getResponse().toString());
            return ResponseEntity.ok(CustomResponse.ok("결제 검증 성공", iamportResponse));
        } catch (Exception e) {
            log.error("결제 검증 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomResponse.error("결제 검증 실패: " + e.getMessage()));
        }
    }
}
