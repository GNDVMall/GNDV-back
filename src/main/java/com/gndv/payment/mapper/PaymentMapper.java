package com.gndv.payment.mapper;

import com.gndv.payment.domain.entity.LocalPayment;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface PaymentMapper {
    @Insert("INSERT INTO LocalPayment (price, status, payment_uid, member_id) VALUES (#{price}, #{status}, #{payment_uid}, #{member_id})")
    @Options(useGeneratedKeys = true, keyProperty = "payment_id")
    void save(LocalPayment payment);

    @Select("SELECT * FROM LocalPayment WHERE payment_id = #{payment_id}")
    Optional<LocalPayment> findById(Long payment_id);

    @Select("SELECT * FROM LocalPayment WHERE payment_uid = #{payment_uid}")
    Optional<LocalPayment> findByUid(String payment_uid);

    @Update("UPDATE LocalPayment SET price = #{price}, status = #{status}, payment_uid = #{payment_uid}, member_id = #{member_id} WHERE payment_id = #{payment_id}")
    void update(LocalPayment payment);

    @Delete("DELETE FROM LocalPayment WHERE payment_id = #{payment_id}")
    void delete(Long payment_id);
}