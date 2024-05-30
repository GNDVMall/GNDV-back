package com.gndv.payment.mapper;


import com.gndv.payment.domain.entity.LocalPayment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaymentMapper {

    @Select("SELECT * FROM Payments WHERE id = #{id}")
    LocalPayment findById(@Param("id") Long id);
}