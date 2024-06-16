package com.gndv.item.mapper;

import com.gndv.item.domain.dto.response.ItemDetailResponse;
import com.gndv.item.domain.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ItemMapper {

    @Select("SELECT i.*, COUNT(*) - 1 as wish_count FROM Item i\n" +
            "LEFT JOIN Wish w ON i.item_id = w.item_id\n" +
            "WHERE i.item_id = #{item_id} GROUP BY i.item_id")
    Optional<ItemDetailResponse> findById(Long item_id);

    @Select("SELECT * FROM Item i")
    List<ItemDetailResponse> findAll();

    @Select("<script>" +
            "SELECT * FROM Item " +
            "WHERE item_name LIKE CONCAT('%', #{keyword}, '%') " +
            "<if test='minPrice != null'>AND recent_price &gt;= #{minPrice}</if> " +
            "<if test='maxPrice != null'>AND recent_price &lt;= #{maxPrice}</if> " +
            "<if test='ageRange != null'>AND age_range = #{ageRange}</if> " +
            "<if test='pieces != null'>AND pieces = #{pieces}</if> " +
            "</script>")
    List<Item> findItems(@Param("keyword") String keyword,
                         @Param("minPrice") Double minPrice,
                         @Param("maxPrice") Double maxPrice,
                         @Param("ageRange") Integer ageRange,
                         @Param("pieces") Integer pieces);
}
