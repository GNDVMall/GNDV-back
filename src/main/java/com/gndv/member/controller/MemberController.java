package com.gndv.member.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.JoinRequest;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
}
