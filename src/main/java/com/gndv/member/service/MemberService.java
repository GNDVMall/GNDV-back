package com.gndv.member.service;

import com.gndv.constant.Status;
import com.gndv.member.domain.dto.request.EditRequest;
import com.gndv.member.domain.dto.request.JoinRequest;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

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
    @PreAuthorize("#email == authentication.name")
    public void editMember(Long member_id, String email, EditRequest request) {
        String encodedPassword = null;
        if (request.getPassword() != null) {
            encodedPassword = passwordEncoder.encode(request.getPassword());
        }

        memberMapper.update(member_id, email, encodedPassword, request.getNickname(), request.getPhone(), request.getIntroduction());
    }

    @Transactional
    public void updateProfileImage(Long member_id, String profile_url) {
        memberMapper.updateProfileImage(member_id, profile_url);
    }

    @Transactional
    @PreAuthorize("#email == authentication.name")
    public void removeMember(Long member_id, String email) {
        memberMapper.delete(member_id);
    }
}
