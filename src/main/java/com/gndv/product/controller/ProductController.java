package com.gndv.product.controller;

import com.gndv.common.CustomResponse;
import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.product.domain.dto.response.ProductDetailResponse;
import com.gndv.product.domain.dto.response.ProductListResponse;
import com.gndv.product.domain.dto.response.ProductResponse;
import com.gndv.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{product_id}")
    public CustomResponse<ProductDetailResponse> getProduct(@PathVariable Long product_id) throws Exception {
        log.info("Get a Product");
        ProductDetailResponse findItem = productService.getProduct(product_id);
        return CustomResponse.ok("Get a Product", findItem);
    }

    @GetMapping("")
    public CustomResponse<ProductListResponse> getProducts(){
        log.info("Get Products");
        List<ProductResponse> products = productService.getProducts();

        return CustomResponse.ok("Get Products", ProductListResponse.builder().products(products).total(products.size()).build());
    }

    @PostMapping("")
    public CustomResponse<Object> insertProduct(@RequestBody ProductInsertRequest request){
        log.info("Insert New Product {}", request);
        // member_id는 로그인 시, 시큐리티 컨텍스트와 입력값을 비교하기
        productService.insertProduct(request);
        return CustomResponse.ok("Insert new Product");
    }
}
