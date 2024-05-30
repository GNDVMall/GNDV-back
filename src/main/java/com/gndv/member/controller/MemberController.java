package com.gndv.member.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.EditRequest;
import com.gndv.member.domain.dto.JoinRequest;
import com.gndv.member.domain.dto.MemberContext;
import com.gndv.member.domain.dto.MemberDTO;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/new")
    public CustomResponse<JoinRequest> createMember(@RequestBody JoinRequest request) {
        log.info("JoinRequest : {}", request);
        memberService.createMember(request);
        return CustomResponse.ok("createMember", request);
    }

    @GetMapping("/{member_id}")
    @PreAuthorize("isAuthenticated()")
    public CustomResponse<Optional<Member>> getMember(@PathVariable Long member_id) {
        Optional<Member> member = memberService.getMember(member_id);
        return CustomResponse.ok("getMember", member);
    }

    @PutMapping("/{member_id}/edit/{email}")
    @PreAuthorize("#email == authentication.name")
    public CustomResponse<EditRequest> editMember(@PathVariable Long member_id, @PathVariable String email, @RequestBody EditRequest request) {
        memberService.editMember(member_id, request);
        return CustomResponse.ok("modify", request);
    }

    @DeleteMapping("/{member_id}/delete/{email}")
    @PreAuthorize("#email == authentication.name")
    public CustomResponse<Object> deleteMember(@PathVariable Long member_id, @PathVariable String email) {
        memberService.removeMember(member_id);
        return CustomResponse.ok("deleteMemberById", null);
    }
}