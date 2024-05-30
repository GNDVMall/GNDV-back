package com.gndv.product.service;

import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.product.domain.dto.request.ProductUpdateRequest;
import com.gndv.product.domain.dto.response.ProductDetailResponse;
import com.gndv.product.domain.dto.response.ProductResponse;
import com.gndv.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
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
        return productMapper.findAll();
    }

    public void insertProduct(ProductInsertRequest request) {
        productMapper.insert(request);
    }

    public int updateProduct(ProductUpdateRequest request) {
        return productMapper.update(request);
    }

    public int deleteProduct(Long product_id, Long member_id) {
        return productMapper.delete(product_id, member_id);
    }

    public ProductInsertRequest findProductInsertRequestById(Long product_id) {
        Optional<ProductDetailResponse> productDetail = productMapper.findById(product_id);
        log.info("productDetail = {}", productDetail) ;
        if (productDetail.isPresent()) {
            ProductDetailResponse detail = productDetail.get();
            ProductInsertRequest productInsertRequest = new ProductInsertRequest();
            productInsertRequest.setMember_id(detail.getMember_id());
            productInsertRequest.setItem_id(detail.getItem_id());
            productInsertRequest.setTitle(detail.getTitle());
            productInsertRequest.setPrice(detail.getPrice());
            productInsertRequest.setContent(detail.getContent());
            productInsertRequest.setProduct_status(detail.getProduct_status());
            productInsertRequest.setProduct_trade_opt1(detail.getProduct_trade_opt1());
            productInsertRequest.setProduct_trade_opt2(detail.getProduct_trade_opt2());
            return productInsertRequest;
        }
        return null;
    }
}