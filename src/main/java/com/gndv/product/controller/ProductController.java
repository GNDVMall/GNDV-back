package com.gndv.product.controller;

import com.gndv.common.CustomResponse;
import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.product.domain.dto.request.ProductUpdateRequest;
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
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{product_id}")
    public CustomResponse<ProductDetailResponse> getProduct(@PathVariable Long product_id) throws Exception {
        log.info("Get a Product");
        ProductDetailResponse findItem = productService.getProduct(product_id);
        return CustomResponse.ok("Get a Product", findItem);
    }

    @GetMapping("")
    public CustomResponse<ProductListResponse> getProducts() {
        log.info("Get Products");
        List<ProductResponse> products = productService.getProducts();

        return CustomResponse.ok("Get Products", ProductListResponse.builder().products(products).total(products.size()).build());
    }

    @PostMapping("")
    public CustomResponse insertProduct(@RequestBody ProductInsertRequest request) {
        log.info("Insert New Product {}", request);
        productService.insertProduct(request);
        return CustomResponse.ok("Insert new Product", null);
    }

    @PutMapping("/{product_id}")
    public CustomResponse<Integer> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long product_id) {
        log.info("Update a Product {}", request);
        request.setProduct_id(product_id);
        int updated = productService.updateProduct(request);
        return CustomResponse.ok("Update a Product", updated);
    }

    @DeleteMapping("/{product_id}")
    public CustomResponse<Integer> deleteProduct(@PathVariable Long product_id){
        log.info("Delete a Product");
        int updated = productService.deleteProduct(product_id);
        return CustomResponse.ok("Delete a Product", updated);
    }
}
