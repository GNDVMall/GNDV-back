package com.gndv.payment.mapper;

import com.gndv.payment.domain.entity.LocalPayment;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PaymentMapper {

    @Select("SELECT * FROM Gangnum_Payment WHERE payment_id = #{payment_id}")
    LocalPayment findById(Long payment_id);

    @Insert("INSERT INTO Gangnum_Payment (price, status, payment_uid) VALUES (#{price}, #{status}, #{payment_uid})")
    @Options(useGeneratedKeys = true, keyProperty = "payment_id")
    void insert(LocalPayment localPayment);

    @Update("UPDATE Gangnum_Payment SET price = #{price}, status = #{status}, payment_uid = #{payment_uid} WHERE payment_id = #{payment_id}")
    void update(LocalPayment localPayment);

    @Delete("DELETE FROM Gangnum_Payment WHERE payment_id = #{payment_id}")
    void delete(Long payment_id);
}
