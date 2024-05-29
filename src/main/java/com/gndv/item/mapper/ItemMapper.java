package com.gndv.item.mapper;

import com.gndv.item.domain.dto.response.ItemDetailResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ItemMapper {
    @Select("SELECT * FROM Item i INNER JOIN Image i2 ON i.item_id = i2.use_id WHERE i2.image_type = 'item' AND i.item_id = #{item_id}")
    Optional<ItemDetailResponse> findById(Long item_id);

    @Select("SELECT * FROM Item i INNER JOIN Image i2 ON i.item_id = i2.use_id WHERE i2.image_type = 'item'")
    List<ItemDetailResponse> findAll();
}
