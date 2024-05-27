package com.gndv.product.service;

import com.gndv.product.domain.dto.response.ProductDetailResponse;
import com.gndv.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;
    public ProductDetailResponse getProduct(Long product_id) throws Exception {
        Optional<ProductDetailResponse> findById = productMapper.findById(product_id);

        if(findById.isPresent()){
            return findById.get();
        }
        throw new Exception();
    }
}
