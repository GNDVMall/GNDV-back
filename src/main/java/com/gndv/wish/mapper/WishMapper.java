package com.gndv.wish.mapper;

import com.gndv.wish.domain.dto.WishDTO;
import com.gndv.wish.domain.entity.Wish;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface WishMapper {
    @Select("SELECT * FROM Wish WHERE member_id = #{memberId}")
    @Results({
            @Result(column = "wish_id", property = "wishId"),
            @Result(column = "member_id", property = "memberId"),
            @Result(column = "item_id", property = "itemId"),
            @Result(column = "created_at", property = "createdAt")
    })
    List<Wish> findByMemberId(Long memberId);

    @Select("SELECT COUNT(*) > 0 FROM Wish WHERE member_id = #{memberId} AND item_id = #{itemId}")
    boolean existsByMemberIdAndItemId(Long memberId, Long itemId);

    @Delete("DELETE FROM Wish WHERE member_id = #{memberId} AND item_id = #{itemId}")
    void deleteByMemberIdAndItemId(Long memberId, Long itemId);

    @Insert("INSERT INTO Wish (member_id, item_id, created_at) VALUES (#{memberId}, #{itemId}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "wishId")
    void insertWish(WishDTO wishDTO);
}