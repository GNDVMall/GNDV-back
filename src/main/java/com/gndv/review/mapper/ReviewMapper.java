package com.gndv.review.mapper;

import com.gndv.review.domain.entity.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviewMapper {

    @Insert("INSERT INTO Review (review_content, review_rating, review_type, member_id) VALUES (#{review_content}, #{review_rating}, #{review_type}, #{member_id})")
    @Options(useGeneratedKeys = true, keyProperty = "review_id")
    void save(Review review);

    @Select("SELECT * FROM Review WHERE review_id = #{review_id}")
    Review findById(Long review_id);

    @Select("SELECT * FROM Review WHERE member_id = #{member_id}")
    List<Review> findByMemberId(Long member_id);

    @Update("UPDATE Review SET review_content = #{review_content}, review_rating = #{review_rating}, review_type = #{review_type}, review_report_count = #{review_report_count} WHERE review_id = #{review_id}")
    void update(Review review);
}
