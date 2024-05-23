package com.gndv.member.service;

import com.gndv.member.domain.dto.JoinRequest;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final ModelMapper mapper;

    public int save(Member member) {
        validateDuplicationMember(member);
        return memberMapper.save(member);
    }

    private void validateDuplicationMember(Member member) {
        Optional<Member> findMember = memberMapper.findByEmail(member.getEmail());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }

    public Member createMember(JoinRequest request) {
        // ModelMapper를 이용하여 JoinRequest를 Member로 변환
        return mapper.map(request, Member.class);
    }
}
