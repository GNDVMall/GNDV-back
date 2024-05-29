package com.gndv.member.mapper;

import com.gndv.constant.Role;
import com.gndv.constant.Status;
import com.gndv.member.domain.entity.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {

    @Select("SELECT * FROM Member WHERE member_id = #{member_id}")
    Optional<Member> findById(Long member_id);

    @Select("SELECT * FROM Member WHERE email = #{email}")
    Optional<Member> findByEmail(@Param("email") String email);

    @Select("SELECT * FROM member")
    List<Member> findAll();

    @Insert("INSERT INTO Member (email, password) VALUES (#{email}, #{password})")
    void insert(Member member);

    @Update("UPDATE Member SET email=#{email}, password=#{password}, nickname=#{nickname}, phone=#{phone}, introduction=#{introduction}" +
            "WHERE member_id=#{member_id}")
    void update(Member member);

    @Delete("DELETE FROM Member WHERE member_id = #{member_id}")
    void delete(Long member_id);
}
