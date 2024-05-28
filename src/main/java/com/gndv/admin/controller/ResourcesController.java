package com.gndv.admin.controller;

import com.gndv.admin.mapper.RoleMapper;
import com.gndv.admin.service.ResourcesService;
import com.gndv.admin.service.RoleService;
import com.gndv.member.domain.dto.ResourcesDTO;
import com.gndv.member.domain.entity.Resources;
import com.gndv.member.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ResourcesController {

    private final ResourcesService resourcesService;
    private final RoleService roleService;

    @GetMapping(value = "/admin/resources")
    public String getResources(Model model) {
        List<Resources> resources = resourcesService.getResources();
        model.addAttribute("resources", resources);

        return "admin/resources";
    }

    @PostMapping(value = "/admin/resources")
    public String createResources(ResourcesDTO resourcesDTO) {
        Role role = roleService.getRoleByRoleName(resourcesDTO.getRole_name());
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        Resources resources = Resources.builder()
                .resource_name(resourcesDTO.getResource_name())
                .resource_type(resourcesDTO.getResource_type())
                .http_method(resourcesDTO.getHttp_method())
                .order_num(resourcesDTO.getOrder_num())
                .roleSet(roles)
                .build();

        resourcesService.createResources(resources);

        return "redirect:/admin/resources";
    }

    @GetMapping(value = "/admin/resources/register")
    public String resourcesRegister(Model model) {
        List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);

        List<String> myRoles = new ArrayList<>();
        model.addAttribute("myRoles", myRoles);

        ResourcesDTO resourcesDTO = new ResourcesDTO();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role());
        resourcesDTO.setRoleSet(roleSet);

        model.addAttribute("resources", resourcesDTO);

        return "admin/resourcesdetails";
    }

    @GetMapping(value = "/admin/resources/{resource_id}")
    public String resourceDetails(@PathVariable String resource_id, Model model) {
        List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);

        Resources resources = resourcesService.getResources(Long.parseLong(resource_id));
        List<String> myRoles = resources.getRoleSet().stream()
                .map(Role::getRole_name)
                .collect(Collectors.toList());
        model.addAttribute("myRoles", myRoles);

        ResourcesDTO resourcesDTO = ResourcesDTO.builder()
                .resource_name(resources.getResource_name())
                .resource_type(resources.getResource_type())
                .http_method(resources.getHttp_method())
                .order_num(resources.getOrder_num())
                .roleSet(resources.getRoleSet())
                .build();

        model.addAttribute("resources", resourcesDTO);

        return "admin/resourcesdetails";
    }

    @GetMapping(value = "/admin/resources/delete/{resource_id}")
    public String removeResources(@PathVariable String resource_id) throws Exception {
        resourcesService.deleteResources(Long.parseLong(resource_id));
        return "redirect:/admin/resources";
    }
}
