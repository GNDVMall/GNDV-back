package com.gndv.product.service;

import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.product.domain.dto.request.ProductUpdateRequest;
import com.gndv.product.domain.dto.response.ProductDetailResponse;
import com.gndv.product.domain.dto.response.ProductResponse;
import com.gndv.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;

    public ProductDetailResponse getProduct(Long product_id) throws Exception {
        Optional<ProductDetailResponse> findById = productMapper.findById(product_id);

        if (findById.isPresent()) {
            return findById.get();
        }
        throw new Exception();
    }

    public List<ProductResponse> getProducts() {
        List<ProductResponse> findList = productMapper.findAll();
        return findList;
    }

    public void insertProduct(ProductInsertRequest request) {
        productMapper.insert(request);
    }

    public int updateProduct(ProductUpdateRequest request) {
        int updated = productMapper.update(request);
        return updated;
    }

    public int deleteProduct(Long product_id) {
        int update = productMapper.delete(product_id);
        return update;
    }
}