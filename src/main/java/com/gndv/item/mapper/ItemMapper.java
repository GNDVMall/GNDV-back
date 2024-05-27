package com.gndv.item.mapper;

import com.gndv.item.domain.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface ItemMapper {
    @Select("SELECT * FROM Item WHERE item_id = #{item_id}")
    Optional<Item> findById(Long item_id);
}
