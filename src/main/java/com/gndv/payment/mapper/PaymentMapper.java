package com.gndv.payment.mapper;

import com.gndv.payment.domain.entity.LocalPayment;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface PaymentMapper {
    @Insert("INSERT INTO Gangnum_Payment (price, status, payment_uid, member_id) VALUES (#{price}, #{status}, #{payment_uid}, #{member_id})")
    @Options(useGeneratedKeys = true, keyProperty = "payment_id")
    void save(LocalPayment payment);

    @Select("SELECT * FROM Gangnum_Payment WHERE payment_id = #{payment_id}")
    Optional<LocalPayment> findById(Long payment_id);

    @Select("SELECT * FROM Gangnum_Payment WHERE payment_uid = #{payment_uid}")
    Optional<LocalPayment> findByUid(String payment_uid);


}