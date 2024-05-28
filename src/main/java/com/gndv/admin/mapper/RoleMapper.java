package com.gndv.admin.mapper;

import com.gndv.member.domain.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper
public interface RoleMapper {

    @Select("SELECT * FROM Role WHERE role_id = #{role_id}")
    Optional<Role> findById(Long role_id);

    @Select("SELECT * FROM Role WHERE role_name = #{role_name}")
    Optional<Role> findByRoleName(String role_name);

    @Select("SELECT * FROM Role")
    List<Role> findAll();

    @Select("SELECT r FROM Role r WHERE r.is_expression = 'N'")
    List<Role> findAllRolesWithoutExpression();

    @Insert("INSERT INTO Role (role_name, role_desc, is_expression) VALUES (#{role_name}, #{role_desc}, #{is_expression})")
    @Options(useGeneratedKeys = true, keyProperty = "role_id")
    Role save(Role role);

    @Delete("DELETE FROM Role WHERE role_id = #{role_id}")
    void deleteById(Long role_id);

    @Insert({
            "<script>",
            "INSERT INTO Role (member_id, role_id) VALUES ",
            "<foreach collection='roles' item='role' separator=','>",
            "(#{member_id}, #{role.role_id})",
            "</foreach>",
            "</script>"
    })
    void insertRoles(@Param("member_id") Long member_id, @Param("roles") Set<Role> roles);

    @Delete("DELETE FROM Role WHERE member_id = #{member_id}")
    void deleteRolesByMemberId(Long member_id);
}
