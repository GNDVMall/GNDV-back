package com.gndv.security.mapper;

import com.gndv.admin.mapper.ResourcesMapper;
import com.gndv.member.domain.entity.Resources;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PersistentUrlRoleMapper implements UrlRoleMapper {

    private final LinkedHashMap<String, String> urlRoleMappings = new LinkedHashMap<>();
    private final ResourcesMapper resourcesMapper;

    public PersistentUrlRoleMapper(ResourcesMapper resourcesMapper) {
        this.resourcesMapper = resourcesMapper;
    }

    @Override
    public Map<String, String> getUrlRoleMappings() {
        List<Resources> resourcesList = resourcesMapper.findAllResources();
        resourcesList.forEach(re -> {
            re.getRoleSet().forEach(role -> {
                urlRoleMappings.put(re.getResource_name(), role.getRole_name());
            });
        });

        return urlRoleMappings;
    }
}