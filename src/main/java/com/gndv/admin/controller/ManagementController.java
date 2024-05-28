package com.gndv.admin.controller;

import com.gndv.admin.service.ManagementService;
import com.gndv.admin.service.RoleService;
import com.gndv.member.domain.dto.MemberDTO;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ManagementController {

    private final ManagementService managementService;
    private final RoleService roleService;

    @GetMapping(value = "/admin/users")
    public String getUsers(Model model) {
        List<Member> users = managementService.getMembers();
        model.addAttribute("users", users);

        return "admin/users";
    }

    @PostMapping(value = "/admin/users")
    public String modifyUser(MemberDTO memberDTO) {
        managementService.modifyMember(memberDTO);
        return "redirect:/admin/users";
    }

    @GetMapping(value = "/admin/users/{member_id}")
    public String getUser(@PathVariable(value = "member_id") Long member_id, Model model) {
        MemberDTO memberDTO = managementService.getMember(member_id);
        List<Role> roleList = roleService.getRolesWithoutExpression();

        model.addAttribute("user", memberDTO);
        model.addAttribute("roleList", roleList);

        return "admin/userdetails";
    }

    @GetMapping(value = "/admin/users/delete/{member_id}")
    public String removeUser(@PathVariable(value = "member_id") Long member_id) {
        managementService.deleteMember(member_id);
        return "redirect:admin/users";
    }
}