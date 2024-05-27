package com.gndv.product.mapper;

import com.gndv.product.domain.dto.response.ProductDetailResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface ProductMapper {
    @Select("SELECT *, GROUP_CONCAT(i.image_id) AS image_ids FROM Product p JOIN Image i ON p.product_id = i.use_id WHERE i.image_type = 'product' AND p.product_id = #{product_id} GROUP BY p.product_id")
    Optional<ProductDetailResponse> findById(Long product_id);
}
