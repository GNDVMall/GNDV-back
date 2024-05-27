package com.gndv.admin.mapper;

import com.gndv.member.domain.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleHierarchyMapper {

    @Select("SELECT * FROM Role WHERE role_name = #{role_name}")
    Role findByRoleName(String role_name);
}
