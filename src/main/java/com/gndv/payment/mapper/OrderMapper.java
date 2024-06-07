package com.gndv.payment.mapper;

import com.gndv.payment.domain.entity.LocalPayment;
import com.gndv.payment.domain.entity.OrderList;
import com.gndv.payment.domain.entity.Orders;
import com.gndv.product.domain.dto.request.ProductInsertWithMemberRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO Gangnum_Payment (price, status, payment_uid, member_id) VALUES (#{price}, #{status}, #{payment_uid}, #{member_id})")
    @Options(useGeneratedKeys = true, keyProperty = "payment_id")
    void savePayment(LocalPayment payment);

    @Insert("INSERT INTO Orders (buyer_id, seller_id, price, item_name, order_uid, payment_id) VALUES (#{buyer_id}, #{seller_id}, #{price}, #{item_name}, #{order_uid}, #{payment_id})")
    @Options(useGeneratedKeys = true, keyProperty = "order_id")
    void save(Orders order);

    @Insert("INSERT INTO Order_List (order_id, payment_id, seller_id, buyer_id) VALUES (#{order_id}, #{payment_id}, #{seller_id}, #{buyer_id})")
    void saveOrderList(OrderList orderList);

    @Select("SELECT * FROM Orders WHERE order_uid = #{orderUid}")
    @Results({
            @Result(column = "order_id", property = "order_id"),
            @Result(column = "order_uid", property = "order_uid"),
            @Result(column = "buyer_id", property = "buyer_id"),
            @Result(column = "seller_id", property = "seller_id"),
            @Result(column = "price", property = "price"),
            @Result(column = "item_name", property = "item_name"),
            @Result(column = "payment_id", property = "payment_id"),
            @Result(column = "buyer_id", property = "buyer", one = @One(select = "com.gndv.member.mapper.MemberMapper.findById")),
            @Result(column = "seller_id", property = "seller", one = @One(select = "com.gndv.member.mapper.MemberMapper.findById")),
            @Result(column = "payment_id", property = "payment", one = @One(select = "com.gndv.payment.mapper.OrderMapper.findPaymentById"))
    })
    Optional<Orders> findOrderAndPaymentAndMember(String orderUid);

    @Select("SELECT * FROM Gangnum_Payment WHERE payment_id = #{paymentId}")
    LocalPayment findPaymentById(Long paymentId);

    @Update("UPDATE Orders SET buyer_id = #{buyer_id}, seller_id = #{seller_id}, price = #{price}, item_name = #{item_name}, payment_id = #{payment_id} WHERE order_id = #{order_id}")
    void update(Orders order);

    @Delete("DELETE FROM Orders WHERE order_id = #{orderId}")
    void delete(Long orderId);

    @Select("SELECT * FROM Orders WHERE buyer_id = #{buyerId}")
    @Results({
            @Result(column = "order_id", property = "order_id"),
            @Result(column = "order_uid", property = "order_uid"),
            @Result(column = "buyer_id", property = "buyer_id"),
            @Result(column = "seller_id", property = "seller_id"),
            @Result(column = "price", property = "price"),
            @Result(column = "item_name", property = "item_name"),
            @Result(column = "payment_id", property = "payment_id"),
            @Result(column = "buyer_id", property = "buyer", one = @One(select = "com.gndv.member.mapper.MemberMapper.findById")),
            @Result(column = "seller_id", property = "seller", one = @One(select = "com.gndv.member.mapper.MemberMapper.findById")),
            @Result(column = "payment_id", property = "payment", one = @One(select = "com.gndv.payment.mapper.OrderMapper.findPaymentById"))
    })
    List<Orders> findOrdersByBuyerId(Long buyerId);

    @Select("SELECT * FROM Product WHERE product_id = #{productId}")
    ProductInsertWithMemberRequest findProductInsertRequestById(Long productId);
    @Select("SELECT * FROM Orders WHERE seller_id = #{sellerId}")
    @Results({
            @Result(column = "order_id", property = "order_id"),
            @Result(column = "order_uid", property = "order_uid"),
            @Result(column = "buyer_id", property = "buyer_id"),
            @Result(column = "seller_id", property = "seller_id"),
            @Result(column = "price", property = "price"),
            @Result(column = "item_name", property = "item_name"),
            @Result(column = "payment_id", property = "payment_id"),
            @Result(column = "buyer_id", property = "buyer", one = @One(select = "com.gndv.member.mapper.MemberMapper.findById")),
            @Result(column = "seller_id", property = "seller", one = @One(select = "com.gndv.member.mapper.MemberMapper.findById")),
            @Result(column = "payment_id", property = "payment", one = @One(select = "com.gndv.payment.mapper.OrderMapper.findPaymentById"))
    })
    List<Orders> findOrdersBySellerId(Long sellerId);
}
