package com.gndv.member.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.JoinRequest;
import com.gndv.member.domain.dto.MemberDTO;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/new")
    public CustomResponse<JoinRequest> create(@RequestBody JoinRequest request) {
        memberService.save(request);
        return CustomResponse.ok("create", request);
    }

    @GetMapping("/{member_id}")
    public CustomResponse<Optional<Member>> getMemberById(@PathVariable Long member_id) {
        return CustomResponse.ok("getMemberById", memberService.findById(member_id));
    }

    @GetMapping
    public CustomResponse<List<Member>> getAllMembers() {
        return CustomResponse.ok("getAllMembers", memberService.findAll());
    }

    @DeleteMapping("/{member_id}")
    public CustomResponse<Void> deleteMemberById(@PathVariable Long member_id) {
        memberService.deleteById(member_id);
        return CustomResponse.ok("deleteMemberById", null);
    }

    @GetMapping(value="/user")
    public MemberDTO restUser(@AuthenticationPrincipal MemberDTO memberDTO) {
        return memberDTO;
    }

    @GetMapping(value="/seller")
    public MemberDTO restManager(@AuthenticationPrincipal MemberDTO memberDTO) {
        return memberDTO;
    }

    @GetMapping(value="/admin")
    public MemberDTO restAdmin(@AuthenticationPrincipal MemberDTO memberDTO) {
        return memberDTO;
    }
}
