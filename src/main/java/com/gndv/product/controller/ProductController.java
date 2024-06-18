package com.gndv.product.controller;

import com.gndv.common.CustomResponse;
import com.gndv.common.domain.response.PageResponse;
import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.product.domain.dto.request.ProductListPagingRequest;
import com.gndv.product.domain.dto.request.ProductUpdateRequest;
import com.gndv.product.domain.dto.response.ProductDetailResponse;
import com.gndv.product.domain.entity.ProductDetail;
import com.gndv.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2/products")
@Tag(name = "Product API", description = "상품 관련 API")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "상품 조회", description = "특정 상품의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 조회 성공", content = @Content(schema = @Schema(implementation = ProductDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{product_id}")
    public CustomResponse<ProductDetailResponse> getProduct(
            @Parameter(description = "상품 ID", required = true) @PathVariable Long product_id) throws Exception {
        log.info("Get a Product");
        ProductDetailResponse findItem = productService.getProduct(product_id);
        return CustomResponse.ok("Get a Product", findItem);
    }

    @Operation(summary = "상품 목록 조회", description = "상품 목록을 페이징하여 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공", content = @Content(schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("")
    public CustomResponse<PageResponse<ProductDetail>> getProducts(
            @Parameter(description = "페이징 요청 정보", required = true) ProductListPagingRequest pagingRequest) {
        log.info("Get Products");
        List<ProductDetail> products = productService.getProducts(pagingRequest);
        PageResponse<ProductDetail> response = PageResponse.<ProductDetail>builder()
                .size(pagingRequest.getSize())
                .pageNo(pagingRequest.getPageNo())
                .list(products)
                .total(products.size() > 0 ? products.get(0).getTotal() : 0)
                .build();
        return CustomResponse.ok("Get Products", response);
    }

    @Operation(summary = "상품 등록", description = "새로운 상품을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 등록 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("")
    public CustomResponse<Long> insertProduct(
            @Parameter(description = "상품 등록 요청 정보", required = true) @ModelAttribute ProductInsertRequest request) throws Exception {
        log.info("Insert New Product {}", request);
        productService.insertProduct(request);
        return CustomResponse.ok("Insert new Product", request.getProduct_id());
    }

    @Operation(summary = "상품 수정", description = "특정 상품의 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 수정 성공", content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{product_id}")
    public CustomResponse<Integer> updateProduct(
            @Parameter(description = "상품 수정 요청 정보", required = true) @ModelAttribute ProductUpdateRequest request,
            @Parameter(description = "상품 ID", required = true) @PathVariable Long product_id) throws Exception {
        log.info("Update a Product {}", request);
        request.setProduct_id(product_id);
        int updated = productService.updateProduct(request);
        if (updated != 1) throw new Exception(); // 나중에 전역 예외 처리 시, 변경할 부분
        return CustomResponse.ok("Update a Product", updated);
    }

    @Operation(summary = "상품 상태 수정", description = "특정 상품의 상태를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 상태 수정 성공", content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/status/{product_id}")
    public CustomResponse<Integer> updateProductStatus(
            @Parameter(description = "상품 상태 수정 요청 정보", required = true) @RequestBody ProductUpdateRequest request,
            @Parameter(description = "상품 ID", required = true) @PathVariable Long product_id) throws Exception {
        log.info("Update a Product Status {}", request);
        request.setProduct_id(product_id);
        int updated = productService.updateProductStatus(request);
        if (updated != 1) throw new Exception(); // 나중에 전역 예외 처리 시, 변경할 부분
        return CustomResponse.ok("Update a Product Status ", updated);
    }

    @Operation(summary = "상품 삭제", description = "특정 상품을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공", content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{product_id}")
    public CustomResponse<Integer> deleteProduct(
            @Parameter(description = "상품 ID", required = true) @PathVariable Long product_id) throws Exception {
        log.info("Delete a Product");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int updated = productService.deleteProduct(product_id, auth.getName());
        if (updated != 1) throw new Exception(); // 나중에 전역 예외 처리 시, 변경할 부분
        return CustomResponse.ok("Delete a Product", updated);
    }
}
