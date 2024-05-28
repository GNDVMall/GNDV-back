package com.gndv.admin.service;

import com.gndv.admin.mapper.RoleMapper;
import com.gndv.member.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleMapper roleMapper;

    public Role getRole(Long role_id) {
        return roleMapper.findById(role_id).orElse(new Role());
    }

    public Role getRoleByRoleName(String role_name) {
        return roleMapper.findByRoleName(role_name).orElse(new Role());
    }

    public List<Role> getRoles() {
        return roleMapper.findAll();
    }

    @Transactional
    public List<Role> getRolesWithoutExpression() {
        return roleMapper.findAllRolesWithoutExpression();
    }

    @Transactional
    public void createRole(Role role){
        roleMapper.save(role);
    }

    @Transactional
    public void deleteRole(Long role_id) {
        roleMapper.deleteById(role_id);
    }
}
