package com.gndv.member.service;

import com.gndv.member.domain.dto.JoinRequest;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

       memberMapper.save(member);
    }

    public Optional<Member> getMember(Long member_id) {
        return memberMapper.findById(member_id);
    }

    public List<Member> getMembers() {
        return memberMapper.findAll();
    }

    @Transactional
    public void removeMember(Long member_id) {
        memberMapper.deleteById(member_id);
    }
}
