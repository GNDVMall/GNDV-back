package com.gndv.admin.mapper;

import com.gndv.member.domain.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select("SELECT * FROM Role WHERE role_name = #{role_name}")
    Role findByRoleName(String role_name);

    @Select("SELECT r FROM Role r WHERE r.is_expression = 'N'")
    List<Role> findAllRolesWithoutExpression();
}
