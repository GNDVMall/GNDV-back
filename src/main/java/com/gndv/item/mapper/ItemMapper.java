package com.gndv.item.mapper;

import com.gndv.image.domain.entity.Image;
import com.gndv.item.domain.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ItemMapper {
    @Select("SELECT * FROM Item WHERE item_id = #{item_id}")
    Optional<Item> findById(Long item_id);

    @Select("SELECT * from Image WHERE image_id = #{item_id} and image_type = #{image_type}")
    Optional<Image> findImages(Long item_id, String image_type);
}
