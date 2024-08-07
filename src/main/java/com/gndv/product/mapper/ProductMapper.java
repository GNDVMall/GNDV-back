package com.gndv.product.mapper;

import com.gndv.image.domain.dto.ImageRequest;
import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.product.domain.dto.request.ProductListPagingRequest;
import com.gndv.product.domain.dto.request.ProductUpdateRequest;
import com.gndv.product.domain.dto.response.ProductDetailResponse;
import com.gndv.product.domain.entity.ProductDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductMapper {
    @Select("SELECT p.*, m.nickname, m.introduction, m.rating, m.profile_url, m.email, i.theme_name\n" +
            "FROM Product_With_Image p LEFT JOIN `Member` m ON p.member_id = m.member_id\n" +
            "JOIN Item_With_Theme i ON i.item_id = p.item_id\n" +
            "WHERE p.product_id = #{product_id} GROUP BY p.product_id")
    Optional<ProductDetailResponse> findById(Long product_id);


    @Update("UPDATE Product SET view_count = view_count + 1 WHERE product_id = #{product_id}")
    int updatedViewCount(Long product_id);

    @Select("SELECT p.* , GROUP_CONCAT(i.real_filename) AS images, (SELECT COUNT(*) FROM Product WHERE item_id = #{item_id}) AS total" +
            " FROM  Product p LEFT JOIN Image i ON p.product_id = i.use_id GROUP BY p.product_id HAVING p.item_id = #{item_id} order by p.created_at desc LIMIT #{skip}, #{size}")
    List<ProductDetail> findAllById(ProductListPagingRequest pagingRequest);

    @Insert("INSERT INTO Product (item_id, title, price, content, product_status, product_trade_opt1, product_trade_opt2, member_id) VALUES (#{item_id},#{title},#{price}, #{content}, #{product_status}, #{product_trade_opt1}, #{product_trade_opt2},(SELECT member_id FROM Member WHERE email = #{email}))")
    @Options(useGeneratedKeys = true, keyProperty = "product_id", keyColumn = "product_id")
    void insert(ProductInsertRequest request);

    @UpdateProvider(type = SqlBuilder.class, method = "buildUpdateProduct")
    int update(ProductUpdateRequest request);

    @Delete("DELETE FROM Product WHERE product_id = #{product_id} AND member_id = (SELECT member_id FROM Member WHERE email = #{email})")
    int delete(Long product_id, String email);

    @Insert("INSERT INTO Image (image_type, use_id, original_name, real_filename, content_type, size)" +
            "VALUES (#{image_type}, #{use_id}, #{original_name}, #{real_filename}, #{content_type}, #{size})")
    int insertImages(ImageRequest imageRequest);
}

