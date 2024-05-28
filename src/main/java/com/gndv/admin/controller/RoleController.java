package com.gndv.admin.controller;

import com.gndv.admin.service.RoleService;
import com.gndv.member.domain.dto.RoleDTO;
import com.gndv.member.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping(value = "/admin/roles")
    public String getRoles(Model model) {
        List<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);

        return "admin/roles";
    }

    @GetMapping(value = "/admin/roles/register")
    public String rolesRegister(Model model) {
        RoleDTO role = new RoleDTO();
        model.addAttribute("roles", role);

        return "admin/rolesdetails";
    }

    @PostMapping(value = "/admin/roles")
    public String createRole(RoleDTO roleDTO) {
        Role role = Role.builder()
                .role_name(roleDTO.getRole_name())
                .role_desc(roleDTO.getRole_desc())
                .build();
        roleService.createRole(role);

        return "redirect:/admin/roles";
    }

    @GetMapping(value = "/admin/roles/{role_id}")
    public String getRole(@PathVariable String role_id, Model model) {
        Role role = roleService.getRole(Long.parseLong(role_id));

        ModelMapper modelMapper = new ModelMapper();
        RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
        model.addAttribute("roles", roleDTO);

        return "admin/rolesdetails";
    }

    @GetMapping(value = "/admin/roles/delete/{role_id}")
    public String removeRoles(@PathVariable String role_id) {
        roleService.deleteRole(Long.parseLong(role_id));

        return "redirect:/admin/roles";
    }
}
