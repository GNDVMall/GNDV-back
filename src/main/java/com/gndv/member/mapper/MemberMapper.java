package com.gndv.member.mapper;

import com.gndv.member.domain.entity.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {

    @Insert("INSERT INTO Member (email, password) VALUES (#{email}, #{password})")
    int save(Member member);

    @Select("SELECT * FROM Member WHERE member_id = #{member_id}")
    Optional<Member> findById(Long member_id);

    @Select("SELECT * FROM Member WHERE email = #{email}")
    Optional<Member> findByEmail(@Param("email") String email);

    @Select("SELECT * FROM Member")
    List<Member> findAll();

    @Delete("DELETE FROM Member WHERE member_id = #{member_id}")
    void deleteMemberById(Long memberId);

    @Select("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM Member WHERE email = #{email}")
    Boolean existsByEmail(String email);
}
