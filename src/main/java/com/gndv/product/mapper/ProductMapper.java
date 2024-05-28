package com.gndv.product.mapper;

import com.gndv.product.domain.dto.response.ProductDetailResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface ProductMapper {
    @Select("SELECT p.*, m.nickname , m.introduction, m.rating, m.profile_url, GROUP_CONCAT(i.real_filename) AS images FROM  Product p JOIN Image i ON p.product_id = i.use_id JOIN Member_With_Profile m ON p.member_id = m.member_id WHERE i.image_type = 'product' AND p.product_id = #{product_id} GROUP BY p.product_id")
    Optional<ProductDetailResponse> findById(Long product_id);
}
