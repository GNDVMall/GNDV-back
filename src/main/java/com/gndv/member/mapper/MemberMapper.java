package com.gndv.member.mapper;

import com.gndv.constant.Role;
import com.gndv.member.domain.dto.request.ProfileDetailsRequest;
import com.gndv.member.domain.entity.Member;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {

    @Insert("INSERT INTO Member (email, password) VALUES (#{email}, #{password})")
    @Transactional
    void insert(Member member);

    @Select("SELECT * FROM Member WHERE member_id = #{member_id}")
    Optional<Member> findById(Long member_id);

    @Select("SELECT * FROM Member WHERE email = #{email}")
    Optional<Member> findByEmail(String email);

    @Select("SELECT member_id FROM Member WHERE email = #{email}")
    Long findMemberIdByEmail(String email);

    @Select("SELECT member_id, email, nickname, phone, introduction, profile_url, created_at, rating, report_count, last_login, role " +
            "FROM Member WHERE email = #{email}")
    Optional<Member> getMemberProfile(String email);

    @Select("SELECT * FROM MemberProfileView WHERE email = #{email} LIMIT #{limit} OFFSET #{offset}")
    List<ProfileDetailsRequest> getMemberProfileDetails(String email, int offset, int limit);

    @Select("SELECT COUNT(*) FROM MemberProfileView WHERE email = #{email}")
    int countReviewsByEmail(String email);

    @Select("SELECT * FROM Member WHERE refreshToke = #{refreshToke}")
    Optional<Member> findByRefreshToken(String refreshToke);

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
    void updateProfileImage(Long member_id, String profile_url);

    @Update("UPDATE Member SET role = #{role} WHERE member_id = #{member_id}")
    void updateSeller(Long member_id, Role role);

    @Update("UPDATE Member SET last_login = #{last_login} WHERE member_id = #{member_id}")
    void updateLastLogin(Long member_id, Date last_login);

    @Update("UPDATE Member SET accessToken = #{accessToken}, refreshToken = #{refreshToken} WHERE member_id = #{member_id}")
    void updateTokens(Long member_id, String accessToken, String refreshToken);

    @Delete("DELETE FROM Member WHERE member_id = #{member_id}")
    @Transactional
    void delete(Long member_id);
}
