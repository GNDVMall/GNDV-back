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
    private final EmailService emailService;

    private static final String REDIS_PREFIX = "email-verification:";

    @Transactional
    public void createMember(JoinRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        String emailVerificationToken = UUID.randomUUID().toString();

        Member member = Member.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .member_status(Status.PENDING)
                .email_verification_token(emailVerificationToken)
                .is_email_verified(false)
                .build();

        memberMapper.insert(member);
        sendVerificationEmail(member);
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

    public void sendVerificationEmail(Member member) {
        String token = member.getEmail_verification_token();
        redisTemplate.opsForValue().set(REDIS_PREFIX + token, member.getEmail(), 1, TimeUnit.HOURS);

        String verificationLink = "http://localhost:8080/api/v2/members/verify?token=" + token;
        emailService.sendSimpleMessage(member.getEmail(), "Email Verification", verificationLink);
    }

    public boolean verifyEmail(String token) {
        String email = (String) redisTemplate.opsForValue().get(REDIS_PREFIX + token);
        if (email != null) {
            Member member = memberMapper.findByEmail(email).orElse(null);
            if (member != null) {
                memberMapper.updateEmailVerifiedStatus(email, true);
                redisTemplate.delete(REDIS_PREFIX + token);
                return true;
            }
        }
        return false;
    }
}
