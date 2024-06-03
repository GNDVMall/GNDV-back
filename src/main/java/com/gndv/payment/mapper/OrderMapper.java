package com.gndv.payment.mapper;

import com.gndv.payment.domain.entity.Orders;
import com.gndv.product.domain.dto.request.ProductInsertRequest;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface OrderMapper {

    @Select("SELECT o.order_id, o.order_uid, o.buyer_id, o.seller_id, o.item_name, o.price, o.payment_id, " +
            "b.member_id AS buyer_id, b.nickname AS buyer_name, b.email AS buyer_email, b.phone AS buyer_phone, " +
            "s.member_id AS seller_id, s.nickname AS seller_name, s.email AS seller_email, s.phone AS seller_phone, " +
            "p.payment_id, p.price AS payment_price, p.status AS payment_status, p.payment_uid " +
            "FROM Orders o " +
            "JOIN Member b ON o.buyer_id = b.member_id " +
            "JOIN Member s ON o.seller_id = s.member_id " +
            "LEFT JOIN Gangnum_Payment p ON o.payment_id = p.payment_id " +
            "WHERE o.order_uid = #{orderUid}")
    @Results({
            @Result(property = "order_id", column = "order_id"),
            @Result(property = "order_uid", column = "order_uid"),
            @Result(property = "price", column = "price"),
            @Result(property = "item_name", column = "item_name"),
            @Result(property = "payment.payment_id", column = "payment_id"),
            @Result(property = "payment.price", column = "payment_price"),
            @Result(property = "payment.status", column = "payment_status"),
            @Result(property = "payment.payment_uid", column = "payment_uid"),
            @Result(property = "buyer.member_id", column = "buyer_id"),
            @Result(property = "buyer.nickname", column = "buyer_name"),
            @Result(property = "buyer.email", column = "buyer_email"),
            @Result(property = "buyer.phone", column = "buyer_phone"),
            @Result(property = "seller.member_id", column = "seller_id"),
            @Result(property = "seller.nickname", column = "seller_name"),
            @Result(property = "seller.email", column = "seller_email"),
            @Result(property = "seller.phone", column = "seller_phone")
    })
    Optional<Orders> findOrderAndPaymentAndMember(String orderUid);

    @Select("SELECT * FROM Orders WHERE payment_id = #{paymentId}")
    @Results({
            @Result(property = "order_id", column = "order_id"),
            @Result(property = "order_uid", column = "order_uid"),
            @Result(property = "price", column = "price"),
            @Result(property = "item_name", column = "item_name"),
            @Result(property = "buyer_id", column = "buyer_id"),
            @Result(property = "seller_id", column = "seller_id"),
            @Result(property = "payment", column = "payment_id", one = @One(select = "com.gndv.payment.mapper.PaymentMapper.findById")),
            @Result(property = "buyer", column = "buyer_id", one = @One(select = "com.gndv.member.mapper.MemberMapper.findById")),
            @Result(property = "seller", column = "seller_id", one = @One(select = "com.gndv.member.mapper.MemberMapper.findById"))
    })
    Optional<Orders> findOrderAndPaymentAndMemberByPaymentId(Long paymentId);

    @Insert("INSERT INTO Orders (order_uid, price, item_name, buyer_id, seller_id, payment_id) VALUES (#{order_uid}, #{price}, #{item_name}, #{buyer_id}, #{seller_id}, #{payment.payment_id})")
    void save(Orders order);

    @Update("UPDATE Orders SET order_uid = #{order_uid}, price = #{price}, item_name = #{item_name}, buyer_id = #{buyer_id}, seller_id = #{seller_id}, payment_id = #{payment.payment_id} WHERE order_id = #{order_id}")
    void update(Orders order);

    @Delete("DELETE FROM Orders WHERE order_id = #{order_id}")
    void delete(Long orderId);

    // 새로운 메서드 추가
    @Select("SELECT * FROM Product WHERE product_id = #{product_id}")
    ProductInsertRequest findProductInsertRequestById(@Param("product_id") Long product_id);
}
