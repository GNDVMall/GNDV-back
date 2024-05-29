package com.gndv.member.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.EditRequest;
import com.gndv.member.domain.dto.JoinRequest;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public CustomResponse getMember(@PathVariable Long member_id) {
        memberService.getMember(member_id);
        return CustomResponse.ok("getMember");
    }

    @PutMapping("/{member_id}")
    public CustomResponse<EditRequest> editMember(@PathVariable Long member_id, @RequestBody EditRequest request) {
        Optional<Member> findMember = memberService.getMember(member_id);
        if (findMember.isEmpty()) {
            return CustomResponse.failure("Member not found");
        }
        memberService.editMember(member_id, request);
        return CustomResponse.ok("modify", request);
    }

    @DeleteMapping("/{member_id}")
    public CustomResponse<Void> deleteMember(@PathVariable Long member_id) {
        memberService.removeMember(member_id);
        return CustomResponse.ok("deleteMemberById", null);
    }
}
