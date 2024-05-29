package com.gndv.member.mapper;

import com.gndv.constant.Role;
import com.gndv.constant.Status;
import com.gndv.member.domain.dto.JoinRequest;
import com.gndv.member.domain.entity.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {

    @Select("SELECT * FROM member WHERE member_id = #{member_id}")
    @Results({
            @Result(property = "member_id", column = "member_id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "introduction", column = "introduction"),
            @Result(property = "created_at", column = "created_at"),
            @Result(property = "rating", column = "rating"),
            @Result(property = "report_count", column = "report_count"),
            @Result(property = "member_status", column = "member_status", javaType = Status.class),
            @Result(property = "last_login", column = "last_login"),
            @Result(property = "role", column = "role", javaType = Role.class),
            @Result(property = "is_account_non_expired", column = "is_account_non_expired"),
            @Result(property = "is_account_non_locked", column = "is_account_non_locked"),
            @Result(property = "is_credentials_non_expired", column = "is_credentials_non_expired"),
            @Result(property = "is_enabled", column = "is_enabled")
    })
    Optional<Member> findById(Long member_id);

    @Select("SELECT * FROM Member WHERE email = #{email}")
    @Results({
            @Result(property = "member_id", column = "member_id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "introduction", column = "introduction"),
            @Result(property = "created_at", column = "created_at"),
            @Result(property = "rating", column = "rating"),
            @Result(property = "report_count", column = "report_count"),
            @Result(property = "member_status", column = "member_status", javaType = Status.class),
            @Result(property = "last_login", column = "last_login"),
            @Result(property = "role", column = "role", javaType = Role.class),
            @Result(property = "is_account_non_expired", column = "is_account_non_expired"),
            @Result(property = "is_account_non_locked", column = "is_account_non_locked"),
            @Result(property = "is_credentials_non_expired", column = "is_credentials_non_expired"),
            @Result(property = "is_enabled", column = "is_enabled")
    })
    Optional<Member> findByEmail(@Param("email") String email);

    @Select("SELECT * FROM member")
    @Results({
            @Result(property = "member_id", column = "member_id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "introduction", column = "introduction"),
            @Result(property = "created_at", column = "created_at"),
            @Result(property = "rating", column = "rating"),
            @Result(property = "report_count", column = "report_count"),
            @Result(property = "member_status", column = "member_status", javaType = Status.class),
            @Result(property = "last_login", column = "last_login"),
            @Result(property = "role", column = "role", javaType = Role.class),
            @Result(property = "is_account_non_expired", column = "is_account_non_expired"),
            @Result(property = "is_account_non_locked", column = "is_account_non_locked"),
            @Result(property = "is_credentials_non_expired", column = "is_credentials_non_expired"),
            @Result(property = "is_enabled", column = "is_enabled")
    })
    List<Member> findAll();

    @Insert("INSERT INTO Member (email, password) VALUES (#{email}, #{password})")
    void save(Member member);

    @Update("UPDATE Member SET email=#{email}, password=#{password}, nickname=#{nickname}, phone=#{phone}, introduction=#{introduction}, created_at=#{created_at}, rating=#{rating}, report_count=#{report_count}, member_status=#{member_status}, last_login=#{last_login}, role=#{role}, is_account_non_expired=#{is_account_non_expired}, is_account_non_locked=#{is_account_non_locked}, is_credentials_non_expired=#{is_credentials_non_expired}, is_enabled=#{is_enabled} " +
            "WHERE member_id=#{member_id}")
    int update(Member member);

    @Delete("DELETE FROM Member WHERE member_id = #{member_id}")
    void deleteById(Long member_id);
}
