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

    @Insert("INSERT INTO Orders (buyer_id, seller_id, price, item_name, order_uid, payment_id, product_id) VALUES (#{buyer_id}, #{seller_id}, #{price}, #{item_name}, #{order_uid}, #{payment_id}, #{product_id})")
    @Options(useGeneratedKeys = true, keyProperty = "order_id")
    void save(Orders order);

    @Insert("INSERT INTO Order_List (order_id, payment_id, seller_id, buyer_id) VALUES (#{order_id}, #{payment_id}, #{seller_id}, #{buyer_id})")
    void saveOrderList(OrderList orderList);


    @Select("SELECT o.*, b.nickname AS buyer_nickname, b.email AS buyer_email, b.phone AS buyer_phone, s.nickname AS seller_nickname, s.email AS seller_email, s.phone AS seller_phone, p.status AS payment_status " +
            "FROM Orders o " +
            "JOIN Member b ON o.buyer_id = b.member_id " +
            "JOIN Member s ON o.seller_id = s.member_id " +
            "JOIN Gangnum_Payment p ON o.payment_id = p.payment_id " +
            "WHERE o.order_uid = #{orderUid}")
    @Results({
            @Result(column = "order_id", property = "order_id"),
            @Result(column = "order_uid", property = "order_uid"),
            @Result(column = "buyer_id", property = "buyer_id"),
            @Result(column = "seller_id", property = "seller_id"),
            @Result(column = "price", property = "price"),
            @Result(column = "item_name", property = "item_name"),
            @Result(column = "payment_id", property = "payment_id"),
            @Result(column = "product_id", property = "product_id"),
            @Result(column = "buyer_nickname", property = "buyer.nickname"),
            @Result(column = "buyer_email", property = "buyer.email"),
            @Result(column = "buyer_phone", property = "buyer.phone"),
            @Result(column = "seller_nickname", property = "seller.nickname"),
            @Result(column = "seller_email", property = "seller.email"),
            @Result(column = "seller_phone", property = "seller.phone"),
            @Result(column = "payment_status", property = "payment.status")
    })
    Optional<Orders> findOrderAndPaymentAndMember(String orderUid);


    @Select("SELECT * FROM Gangnum_Payment WHERE payment_id = #{paymentId}")
    LocalPayment findPaymentById(Long paymentId);

    @Update("UPDATE Orders SET buyer_id = #{buyer_id}, seller_id = #{seller_id}, price = #{price}, item_name = #{item_name}, payment_id = #{payment_id}, product_id = #{product_id} WHERE order_id = #{order_id}")
    void update(Orders order);

    @Delete("DELETE FROM Orders WHERE order_id = #{order_id}")
    void delete(Long orderId);

    @Select("SELECT o.*, b.nickname AS buyer_nickname, b.email AS buyer_email, b.phone AS buyer_phone, p.status AS payment_status " +
            "FROM Orders o " +
            "JOIN Member b ON o.buyer_id = b.member_id " +
            "JOIN Gangnum_Payment p ON o.payment_id = p.payment_id " +
            "WHERE o.buyer_id = #{buyerId}")
    @Results({
            @Result(column = "order_id", property = "order_id"),
            @Result(column = "order_uid", property = "order_uid"),
            @Result(column = "buyer_id", property = "buyer_id"),
            @Result(column = "seller_id", property = "seller_id"),
            @Result(column = "price", property = "price"),
            @Result(column = "item_name", property = "item_name"),
            @Result(column = "payment_id", property = "payment_id"),
            @Result(column = "product_id", property = "product_id"),
            @Result(column = "buyer_nickname", property = "buyer.nickname"),
            @Result(column = "buyer_email", property = "buyer.email"),
            @Result(column = "buyer_phone", property = "buyer.phone"),
            @Result(column = "payment_status", property = "payment.status")
    })
    List<Orders> findOrdersByBuyerId(Long buyerId);

    @Select("SELECT * FROM Product WHERE product_id = #{productId}")
    ProductInsertWithMemberRequest findProductInsertRequestById(Long productId);

    @Select("SELECT o.*, b.nickname AS buyer_nickname, b.email AS buyer_email, b.phone AS buyer_phone, p.status AS payment_status, r.review_id AS review_id " +
            "FROM Orders o " +
            "JOIN Member b ON o.buyer_id = b.member_id " +
            "JOIN Gangnum_Payment p ON o.payment_id = p.payment_id " +
            "LEFT JOIN Review r ON o.product_id = r.product_id AND o.buyer_id = r.email " + // 리뷰 테이블을 조인 (product_id와 email을 사용하여 조인 조건 수정)
            "WHERE o.seller_id = #{sellerId}")
    @Results({
            @Result(column = "order_id", property = "order_id"),
            @Result(column = "order_uid", property = "order_uid"),
            @Result(column = "buyer_id", property = "buyer_id"),
            @Result(column = "seller_id", property = "seller_id"),
            @Result(column = "price", property = "price"),
            @Result(column = "item_name", property = "item_name"),
            @Result(column = "payment_id", property = "payment_id"),
            @Result(column = "product_id", property = "product_id"),
            @Result(column = "buyer_nickname", property = "buyer.nickname"),
            @Result(column = "buyer_email", property = "buyer.email"),
            @Result(column = "buyer_phone", property = "buyer.phone"),
            @Result(column = "payment_status", property = "payment.status"),
            @Result(column = "review_id", property = "review_id")
    })
    List<Orders> findOrdersBySellerId(Long sellerId);


    @Update("UPDATE Product SET product_sales_status = 'SOLDOUT' WHERE product_id = #{productId}")
    void updateProductStatusToSoldOut(Long productId);

}
