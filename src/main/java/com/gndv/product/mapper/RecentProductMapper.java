package com.gndv.product.mapper;

import com.gndv.product.domain.dto.response.ProductResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RecentProductMapper {

    @Select("SELECT p.*, COALESCE(GROUP_CONCAT(i.real_filename), '') AS images " +
            "FROM Product p LEFT JOIN Image i ON p.product_id = i.use_id AND i.image_type = 'product' " +
            "GROUP BY p.product_id " +
            "ORDER BY p.created_at DESC " +
            "LIMIT 10")
    List<ProductResponse> findRecentProducts();
}
