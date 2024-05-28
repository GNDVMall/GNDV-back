package com.gndv.admin.mapper;

import com.gndv.member.domain.entity.Member;
import com.gndv.member.domain.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper
public interface ManagementMapper {

    @Select("SELECT * FROM Member WHERE member_id = #{member_id}")
    Optional<Member> findById(Long member_id);

    @Select("SELECT * FROM Member")
    List<Member> findAll();

    @Update({
            "<script>",
            "UPDATE Member",
            "<set>",
            "<if test='member_status != null'>member_status = #{member_status},</if>",
            "<if test='last_login != null'>last_login = #{last_login},</if>",
            "<if test='is_account_non_expired != null'>is_account_non_expired = #{is_account_non_expired},</if>",
            "<if test='is_account_non_locked != null'>is_account_non_locked = #{is_account_non_locked},</if>",
            "<if test='is_credentials_non_expired != null'>is_credentials_non_expired = #{is_credentials_non_expired},</if>",
            "<if test='is_enabled != null'>is_enabled = #{is_enabled},</if>",
            "</set>",
            "WHERE member_id = #{member_id}",
            "</script>"
    })
    int update(Member member);

    @Delete("DELETE FROM Member WHERE member_id = #{member_id}")
    void deleteById(Long member_id);
}
