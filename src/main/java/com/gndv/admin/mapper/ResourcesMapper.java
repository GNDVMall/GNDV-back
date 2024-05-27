package com.gndv.admin.mapper;

import com.gndv.member.domain.entity.Resources;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ResourcesMapper {

    @Select("SELECT resource_id FROM Resources WHERE resource_name = #{resource_name}, http_method = #{http_method}")
    Resources findByResourceNameAndHttpMethod(String resource_name, String http_method);

    @Select("SELECT r FROM Resources r JOIN fetch r.roleSet WHERE r.resourceType = 'url' ORDER BY r.orderNum DESC")
    List<Resources> findAllResources();
}
