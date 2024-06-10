package com.gndv.payment.mapper;

import com.gndv.payment.domain.entity.Orders;
import com.gndv.payment.domain.entity.LocalPayment;
import com.gndv.payment.domain.entity.OrderList;
import com.gndv.product.domain.dto.request.ProductInsertWithMemberRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO Orders (order_uid, buyer_id, seller_id, item_name, price, payment_id) VALUES (#{order_uid}, #{buyer_id}, #{seller_id}, #{item_name}, #{price}, #{payment_id})")
    @Options(useGeneratedKeys = true, keyProperty = "order_id", keyColumn = "order_id")
    void save(Orders order);

    @Insert("INSERT INTO Gangnum_Payment (price, status, payment_uid, member_id) VALUES (#{price}, #{status}, #{payment_uid}, #{member_id})")
    @Options(useGeneratedKeys = true, keyProperty = "payment_id", keyColumn = "payment_id")
    void savePayment(LocalPayment payment);

    @Insert("INSERT INTO Order_List (order_id, payment_id, seller_id, buyer_id) VALUES (#{order_id}, #{payment_id}, #{seller_id}, #{buyer_id})")
    void saveOrderList(OrderList orderList);

    @Select("SELECT o.order_id, o.order_uid, o.buyer_id, o.seller_id, o.item_name, o.price, o.payment_id, " +
            "b.member_id AS buyer_id, b.nickname AS buyer_name, b.email AS buyer_email, b.phone AS buyer_phone, " +
            "s.member_id AS seller_id, s.nickname AS seller_name, s.email AS seller_email, s.phone AS seller_phone, " +
            "p.payment_id, p.price AS payment_price, p.status AS payment_status, p.payment_uid, p.member_id AS payment_member_id " +
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
            @Result(property = "payment.member_id", column = "payment_member_id"),
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

    @Update("UPDATE Orders SET item_name = #{item_name}, price = #{price} WHERE order_uid = #{order_uid}")
    void update(Orders order);

    @Delete("DELETE FROM Orders WHERE order_id = #{order_id}")
    void delete(Long orderId);

    @Select("SELECT p.product_id, p.item_id, p.title, p.content, p.price, p.member_id, " +
            "p.product_trade_opt1, p.product_trade_opt2, p.product_status " +
            "FROM Product p " +
            "WHERE p.product_id = #{productId}")
    ProductInsertWithMemberRequest findProductInsertRequestById(Long productId);

    @Select("SELECT * FROM Orders WHERE buyer_id = #{buyerId}")
    List<Orders> findOrdersByBuyerId(Long buyerId);

    @Select("SELECT * FROM Order_List WHERE order_list_id = #{order_list_id}")
    OrderList findOrderListById(@Param("order_list_id") Long order_list_id);
}
