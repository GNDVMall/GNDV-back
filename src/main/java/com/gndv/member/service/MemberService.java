package com.gndv.member.service;

import com.gndv.member.domain.dto.EditRequest;
import com.gndv.member.domain.dto.JoinRequest;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createMember(JoinRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .build();

       memberMapper.insert(member);
    }

    public Optional<Member> getMember(Long member_id) {
        return memberMapper.findById(member_id);
    }

    @Transactional
    public void editMember(Long member_id, EditRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.builder()
                .member_id(member_id)
                .email(request.getEmail())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .phone(request.getPhone())
                .introduction(request.getIntroduction())
                .build();

        memberMapper.update(member);
    }

    @Transactional
    public void removeMember(Long member_id) {
        memberMapper.delete(member_id);
    }
}
