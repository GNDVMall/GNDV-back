package com.gndv.admin.service;

import com.gndv.admin.mapper.ResourcesMapper;
import com.gndv.member.domain.entity.Resources;
import com.gndv.security.manager.CustomDynamicAuthorizationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourcesService {

    private final ResourcesMapper resourcesMapper;
    private final CustomDynamicAuthorizationManager authorizationManager;

    @Transactional
    public Resources getResources(Long resource_id) {
        return resourcesMapper.findById(resource_id).orElse(new Resources());
    }

    @Transactional
    public List<Resources> getResources() {
        return resourcesMapper.findAll();
    }

    @Transactional
    public void createResources(Resources resources) {
        resourcesMapper.save(resources);
        authorizationManager.reload();
    }

    @Transactional
    public void deleteResources(Long resource_id) {
        resourcesMapper.deleteById(resource_id);
        authorizationManager.reload();
    }
}
