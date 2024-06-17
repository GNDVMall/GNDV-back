package com.gndv.product.controller;

import com.gndv.common.CustomResponse;
import com.gndv.product.domain.dto.response.ProductListResponse;
import com.gndv.product.domain.dto.response.ProductResponse;
import com.gndv.product.service.RecentProductService;
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
public class RecentProductController {

    private final RecentProductService recentProductService;

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
