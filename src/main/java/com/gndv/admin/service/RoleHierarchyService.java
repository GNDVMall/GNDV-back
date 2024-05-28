package com.gndv.admin.service;

import com.gndv.admin.mapper.RoleHierarchyMapper;
import com.gndv.member.domain.entity.RoleHierarchy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleHierarchyService {

    private final RoleHierarchyMapper roleHierarchyMapper;

    @Transactional
    public String findAllHierarchy() {
        List<RoleHierarchy> rolesHierarchy = roleHierarchyMapper.findAll();

        Iterator<RoleHierarchy> itr = rolesHierarchy.iterator();
        StringBuilder hierarchyRole = new StringBuilder();

        while (itr.hasNext()) {
            RoleHierarchy roleHierarchy = itr.next();
            if (roleHierarchy.getParent() != null) {
                hierarchyRole.append(roleHierarchy.getParent().getRole_name());
                hierarchyRole.append(" > ");
                hierarchyRole.append(roleHierarchy.getRole_name());
                hierarchyRole.append("\n");
            }
        }

        return hierarchyRole.toString();
    }
}
