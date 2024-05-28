package com.gndv.admin.mapper;

import com.gndv.member.domain.entity.Resources;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ResourcesMapper {

    @Select("SELECT * FROM Resources WHERE resource_id = #{resource_id}")
    Optional<Resources> findById(Long resource_id);

    @Select("SELECT * FROM Resources")
    List<Resources> findAll();

    @Select("SELECT resource_id FROM Resources WHERE resource_name = #{resource_name}, http_method = #{http_method}")
    Resources findByResourceNameAndHttpMethod(String resource_name, String http_method);

    @Select("SELECT r FROM Resources r JOIN fetch r.roleSet WHERE r.resourceType = 'url' ORDER BY r.orderNum DESC")
    List<Resources> findAllResources();

    @Insert("INSERT INTO Resources (resource_name, http_method, order_num, resource_type) VALUES (#{resource_name}, #{http_method}, #{order_num}, #{resource_type})")
    @Options(useGeneratedKeys = true, keyProperty = "resource_id")
    Resources save(Resources resources);

    @Delete("DELETE FROM Resources WHERE resource_id = #{resource_id}")
    void deleteById(Long resource_id);
}
