package com.gndv.member.mapper;

import com.gndv.member.domain.entity.Member;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {

    @Select("SELECT * FROM Member WHERE member_id = #{memberId}")
    @Results({
            @Result(property = "member_id", column = "member_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "email", column = "email")
    })
    Optional<Member> findById(Long memberId);

    @Select("SELECT * FROM Member WHERE email = #{email}")
    Optional<Member> findByEmail(@Param("email") String email);

    @Select("SELECT * FROM member")
    List<Member> findAll();

    @Insert("INSERT INTO Member (email, password) VALUES (#{email}, #{password})")
    @Transactional
    void insert(Member member);

    @Update({
            "<script>",
            "UPDATE Member",
            "<set>" +
            "<if test='email != null'>email = #{email},</if>" +
            "<if test='password != null'>password = #{password},</if>" +
            "<if test='nickname != null'>nickname = #{nickname},</if>" +
            "<if test='phone != null'>phone = #{phone},</if>" +
            "<if test='introduction != null'>introduction = #{introduction},</if>" +
            "</set>" +
            "WHERE member_id = #{member_id}" +
            "</script>"})
    @Transactional
    void update(@Param("member_id") Long member_id,
                @Param("email") String email,
                @Param("password") String password,
                @Param("nickname") String nickname,
                @Param("phone") String phone,
                @Param("introduction") String introduction);

    @Update("UPDATE Member SET profile_url = #{profile_url} WHERE member_id = #{member_id}")
    void updateProfileImage(@Param("member_id") Long member_id, @Param("profile_url") String profile_url);

    @Delete("DELETE FROM Member WHERE member_id = #{member_id}")
    @Transactional
    void delete(Long member_id);

    @Select("SELECT * FROM Member WHERE refreshToke = #{refreshToke}")
    Optional<Member> findByRefreshToken(String refreshToke);

    @Update("UPDATE Member SET accessToken = #{accessToken}, refreshToken = #{refreshToken} WHERE member_id = #{member_id}")
    void updateTokens(Long member_id, String accessToken, String refreshToken);
}
