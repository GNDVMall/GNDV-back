package com.gndv.payment.mapper;

import com.gndv.payment.domain.entity.Orders;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface OrderMapper {

    @Select("SELECT * FROM Orders WHERE order_uid = #{orderUid}")
    @Results({
            @Result(property = "order_id", column = "order_id"),
            @Result(property = "order_uid", column = "order_uid"),
            @Result(property = "price", column = "price"),
            @Result(property = "item_name", column = "item_name"),
            @Result(property = "payment_id", column = "payment_id"),
            @Result(property = "buyer", column = "buyer_id", one = @One(select = "com.gndv.member.mapper.MemberMapper.findById")),
            @Result(property = "seller", column = "seller_id", one = @One(select = "com.gndv.member.mapper.MemberMapper.findById")),
            @Result(property = "payment", column = "payment_id", one = @One(select = "com.gndv.payment.mapper.PaymentMapper.findById"))
    })
    Optional<Orders> findOrderAndPaymentAndMember(@Param("orderUid") String orderUid);

    @Insert("INSERT INTO Orders (order_uid, buyer_id, seller_id, price, item_name) VALUES (#{order_uid}, #{buyer_id}, #{seller_id}, #{price}, #{item_name})")
    @Options(useGeneratedKeys = true, keyProperty = "order_id")
    void save(Orders order);

    @Delete("DELETE FROM Orders WHERE order_id = #{orderId}")
    void delete(Long orderId);
}
