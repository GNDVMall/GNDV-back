package com.gndv.review.mapper;

import com.gndv.review.domain.entity.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReviewMapper {

    @Insert("INSERT INTO Review (review_content, review_rating, review_type, email, product_id) VALUES (#{review_content}, #{review_rating}, #{review_type}, #{email}, #{product_id})")
    @Options(useGeneratedKeys = true, keyProperty = "review_id")
    void save(Review review);

    @Select("SELECT * FROM Review WHERE review_id = #{review_id}")
    Review findById(Long review_id);

    @Select("SELECT * FROM Review WHERE email = #{email}")
    List<Review> findByEmail(String email);

    @Update("UPDATE Review SET review_content = #{review_content}, review_rating = #{review_rating}, review_type = #{review_type}, review_report_count = #{review_report_count} WHERE review_id = #{review_id}")
    void update(Review review);

    @Select("SELECT COUNT(*) > 0 FROM gndv.Review WHERE product_id = #{productId} AND email = #{email}")
    boolean existsByProductIdAndEmail(@Param("productId") Long productId, @Param("email") String email);

    @Select("SELECT p.product_id " +
            "FROM gndv.Order_List o " +
            "JOIN gndv.Orders p ON o.order_id = p.order_id " +
            "WHERE o.order_list_id = #{order_list_id}")
    Long findProductIdByOrderListId(@Param("order_list_id") Long order_list_id);

    @Select("SELECT * FROM Review WHERE member_id = #{member_id} LIMIT #{skip}, #{size}")
    @Results(id = "reviewResultMap", value = {
            @Result(column = "review_id", property = "review_id"),
            @Result(column = "review_content", property = "review_content"),
            @Result(column = "review_rating", property = "review_rating"),
            @Result(column = "review_report_count", property = "review_report_count"),
            @Result(column = "review_type", property = "review_type"),
            @Result(column = "product_id", property = "product_id"),
            @Result(column = "member_id", property = "member_id")
    })
    List<Review> findReviewsByEmail(Long member_id, @Param("skip") int skip, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM Review WHERE member_id = #{member_id}")
    int countReviewsByEmail(Long member_id);
}
