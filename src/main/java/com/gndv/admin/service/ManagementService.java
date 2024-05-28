package com.gndv.admin.service;

import com.gndv.admin.mapper.ManagementMapper;
import com.gndv.admin.mapper.RoleMapper;
import com.gndv.member.domain.dto.MemberDTO;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagementService {

    private final ManagementMapper managementMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberDTO getMember(Long member_id) {
        Member member = managementMapper.findById(member_id).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        ModelMapper modelMapper = new ModelMapper();
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);

        List<String> roles = member.getRoles()
                .stream()
                .map(Role::getRole_name)
                .collect(Collectors.toList());

        memberDTO.setRoles(roles);
        return memberDTO;
    }

    public List<Member> getMembers() {
        return managementMapper.findAll();
    }

    @Transactional
    public void modifyMember(MemberDTO memberDTO) {
        Member existingMember = managementMapper.findById(memberDTO.getMember_id()).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Set<Role> roles = new HashSet<>();
        if (memberDTO.getRoles() != null) {
            memberDTO.getRoles().forEach(role_name -> {
                Role role = roleMapper.findByRoleName(role_name);
                roles.add(role);
            });
        }

        Member member = Member.builder()
                .member_id(existingMember.getMember_id())
                .member_status(existingMember.getMember_status())
                .last_login(existingMember.getLast_login())
                .is_account_non_expired(existingMember.is_account_non_expired())
                .is_account_non_locked(existingMember.is_account_non_locked())
                .is_credentials_non_expired(existingMember.is_credentials_non_expired())
                .is_enabled(existingMember.is_enabled())
                .Roles(roles.isEmpty() ? existingMember.getRoles() : roles)
                .build();

        managementMapper.update(member);

        if (!roles.isEmpty()) {
            roleMapper.deleteRolesByMemberId(member.getMember_id());
            roleMapper.insertRoles(member.getMember_id(), roles);
        }
    }

    @Transactional
    public void deleteMember(Long member_id) {
        managementMapper.deleteById(member_id);
    }
}
