package com.gndv.member.controller;

import com.gndv.member.domain.dto.InsertFormDTO;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/members/new")
    @ResponseBody
    public InsertFormDTO create() {
        return new InsertFormDTO();
    }

    @PostMapping(value = "/members/new")
    public Member create(@RequestBody InsertFormDTO InsertForm) {
        ModelMapper modelMapper = new ModelMapper();
        Member member = modelMapper.map(InsertForm, Member.class);
        return memberService.insertMember(member);
    }

}
