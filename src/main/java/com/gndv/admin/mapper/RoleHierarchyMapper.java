package com.gndv.admin.mapper;

import com.gndv.member.domain.entity.Role;
import com.gndv.member.domain.entity.RoleHierarchy;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleHierarchyMapper {

    @Select("SELECT * FROM role_hierarchy")
    @Results({
            @Result(property = "role_hierarchy_id", column = "role_hierarchy_id"),
            @Result(property = "role_name", column = "role_name"),
            @Result(property = "parent", column = "parent_id", one = @One(select = "findById")),
            @Result(property = "children", column = "role_hierarchy_id", many = @Many(select = "findChildren"))
    })
    List<RoleHierarchy> findAll();
}
