package com.gndv.product.controller;

import com.gndv.common.CustomResponse;
import com.gndv.common.domain.response.PageResponse;
import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.product.domain.dto.request.ProductListPagingRequest;
import com.gndv.product.domain.dto.request.ProductUpdateRequest;
import com.gndv.product.domain.dto.response.ProductDetailResponse;
import com.gndv.product.domain.entity.ProductDetail;
import com.gndv.product.service.ProductService;
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
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{product_id}")
    public CustomResponse<ProductDetailResponse> getProduct(@PathVariable Long product_id) throws Exception {
        log.info("Get a Product");
        ProductDetailResponse findItem = productService.getProduct(product_id);
        return CustomResponse.ok("Get a Product", findItem);
    }

    @GetMapping("")
    public CustomResponse<PageResponse<ProductDetail>> getProducts(ProductListPagingRequest pagingRequest) {
        log.info("Get Products");
        List<ProductDetail> products = productService.getProducts(pagingRequest);
        // builder 사용하는 경우 제네릭은 아래처럼 적용해야함
        PageResponse<ProductDetail> response = PageResponse.<ProductDetail>builder()
                .size(pagingRequest.getSize())
                .pageNo(pagingRequest.getPageNo())
                .list(products)
                .total(products.size() > 0 ? products.get(0).getTotal() : 0)
                .build();
        return CustomResponse.ok("Get Products", response);
    }

    @PostMapping("")
    public CustomResponse insertProduct(@RequestBody ProductInsertRequest request) {
        log.info("Insert New Product {}", request);
        productService.insertProduct(request);
        return CustomResponse.ok("Insert new Product", null);
    }

    @PutMapping("/{product_id}")
    public CustomResponse<Integer> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long product_id) throws Exception {
        log.info("Update a Product {}", request);
        request.setProduct_id(product_id);

        int updated = productService.updateProduct(request);
        if(updated != 1) throw new Exception(); // 나중에 전역 예외 처리 시, 변경할 부분
        return CustomResponse.ok("Update a Product", updated);
    }

    @DeleteMapping("/{product_id}")
    public CustomResponse<Integer> deleteProduct(@PathVariable Long product_id) throws Exception {
        log.info("Delete a Product");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int updated = productService.deleteProduct(product_id, auth.getName());
        if(updated != 1) throw new Exception(); // 나중에 전역 예외 처리 시, 변경할 부분
        return CustomResponse.ok("Delete a Product", updated);
    }
}