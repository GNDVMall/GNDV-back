package com.gndv.product.service;

import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.product.domain.dto.request.ProductListPagingRequest;
import com.gndv.product.domain.dto.request.ProductUpdateRequest;
import com.gndv.product.domain.dto.response.ProductDetailResponse;
import com.gndv.product.domain.entity.ProductDetail;
import com.gndv.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<ProductDetail> getProducts(ProductListPagingRequest pagingRequest) {
        List<ProductDetail> findList = productMapper.findAllById(pagingRequest);
        return findList;
    }


    @Transactional
    @PreAuthorize("#request.email == authentication.name")
    public void insertProduct(ProductInsertRequest request) {
        productMapper.insert(request);
    }

    @Transactional
    @PreAuthorize("#request.email == authentication.name")
    public int updateProduct(ProductUpdateRequest request) {
        int updated = productMapper.update(request);
        return updated;
    }

    @Transactional
//    @PreAuthorize("isAuthenticated()")
    public int deleteProduct(Long product_id, String email) {
        int update = productMapper.delete(product_id, email);
        return update;
    }
}