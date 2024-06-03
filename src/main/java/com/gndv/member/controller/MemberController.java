package com.gndv.member.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.EditRequest;
import com.gndv.member.domain.dto.JoinRequest;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
<<<<<<< HEAD
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
=======
>>>>>>> b6223f4bbf0ca31ad2cac532c7f050b6900eb93f
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/members")
@Slf4j
@EnableMethodSecurity
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/new")
    public CustomResponse<JoinRequest> createMember(@RequestBody JoinRequest request) {
        log.info("JoinRequest : {}", request);
        memberService.createMember(request);
        return CustomResponse.ok("createMember", request);
    }

    @GetMapping("/{member_id}")
    public CustomResponse<Optional<Member>> getMember(@PathVariable Long member_id) {
        Optional<Member> member = memberService.getMember(member_id);
        return CustomResponse.ok("getMember", member);
    }

    @PutMapping("/{member_id}/edit/{email}")
    public CustomResponse<EditRequest> editMember(@PathVariable Long member_id, @PathVariable String email, @RequestBody EditRequest request) {
        memberService.editMember(member_id, email, request);
        return CustomResponse.ok("modify", request);
    }

    @DeleteMapping("/{member_id}/delete/{email}")
    public CustomResponse<Object> deleteMember(@PathVariable Long member_id, @PathVariable String email) {
        memberService.removeMember(member_id, email);
        return CustomResponse.ok("deleteMemberById", null);
    }
}