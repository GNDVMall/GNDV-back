package com.gndv.product.service;

import com.gndv.product.domain.dto.response.ProductResponse;
import com.gndv.product.mapper.RecentProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentProductService {

    private final RecentProductMapper recentProductMapper;

    public List<ProductResponse> getRecentProducts() {
        return recentProductMapper.findRecentProducts();
    }
}
