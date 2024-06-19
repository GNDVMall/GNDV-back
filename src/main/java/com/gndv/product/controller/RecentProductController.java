package com.gndv.product.controller;

import com.gndv.common.CustomResponse;
import com.gndv.product.domain.dto.response.ProductListResponse;
import com.gndv.product.domain.dto.response.ProductResponse;
import com.gndv.product.service.RecentProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/recent-product")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Recent Product API", description = "최근 상품 관련 API")
public class RecentProductController {

    private final RecentProductService recentProductService;

    @Operation(summary = "최근 상품 목록 조회", description = "최근 등록된 상품 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "목록 조회 성공", content = @Content(schema = @Schema(implementation = ProductListResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("")
    public CustomResponse<ProductListResponse> getRecentProducts() throws Exception {
        log.info("Get Recent Product List");
        List<ProductResponse> recentProducts = recentProductService.getRecentProducts();
        ProductListResponse response = ProductListResponse.builder()
                .products(recentProducts)
                .total(recentProducts.size())
                .build();
        log.info("Recent products {}", recentProducts);
        return CustomResponse.ok("Get Recent Products", response);
    }
}
