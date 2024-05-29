package com.gndv.member.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.JoinRequest;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/new")
    public CustomResponse<JoinRequest> createMember(@RequestBody JoinRequest request) {
        log.info("JoinRequest : {}", request);
        memberService.createMember(request);
        return CustomResponse.ok("createMember", request);
    }

    @GetMapping("/members/{member_id}")
    public CustomResponse<Optional<Member>> getMember(@PathVariable Long member_id) {
        return CustomResponse.ok("getMemberById", memberService.getMember(member_id));
    }

    @DeleteMapping("/members/{member_id}")
    public CustomResponse<Void> deleteMember(@PathVariable Long member_id) {
        memberService.removeMember(member_id);
        return CustomResponse.ok("deleteMemberById", null);
    }
}
