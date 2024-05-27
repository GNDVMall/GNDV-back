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
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public int save(JoinRequest request) {
        validateDuplicationMember(request);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Member member = mapper.map(request, Member.class);
        return memberMapper.save(member);
    }

    public Optional<Member> findById(Long member_id) {
        return memberMapper.findById(member_id);
    }

    public Optional<Member> findByEmail(String email) {
        return memberMapper.findByEmail(email);
    }

    public List<Member> findAll() {
        return memberMapper.findAll();
    }

    @Transactional
    public void deleteById(Long member_id) {
        memberMapper.deleteById(member_id);
    }

    public Boolean existsByEmail(String email) {
        return memberMapper.existsByEmail(email);
    }

    private void validateDuplicationMember(JoinRequest request) {
        Optional<Member> findMember = memberMapper.findByEmail(request.getEmail());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }

    @Transactional
    public Member createMember(JoinRequest request) {
        // ModelMapper를 이용하여 JoinRequest를 Member로 변환
        return mapper.map(request, Member.class);
    }
}
