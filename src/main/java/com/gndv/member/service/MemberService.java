package com.gndv.member.service;

import com.gndv.common.domain.request.PagingRequest;
import com.gndv.common.domain.response.PageResponse;
import com.gndv.configs.SmsConfig;
import com.gndv.constant.Role;
import com.gndv.member.domain.dto.request.*;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.review.domain.entity.Review;
import com.gndv.review.mapper.ReviewMapper;
import com.gndv.security.token.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final RedisTemplate<String, Object> redisTemplate;
    private final SmsService smsService;
    private final SmsConfig smsConfig;
    private final TokenProvider tokenProvider;

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

    @Transactional(readOnly = true)
    public ProfileRequest getMemberProfileWithPagedReviews(String email, PagingRequest pagingRequest) {
        Member member = memberMapper.getMemberProfile(email)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        int offset = pagingRequest.getSkip();
        int limit = pagingRequest.getSize();

        List<ProfileDetailsRequest> reviews = memberMapper.getMemberProfileDetails(email, offset, limit);
        int totalReviews = memberMapper.countReviewsByEmail(email);

        PageResponse<ProfileDetailsRequest> reviewPage = new PageResponse<>(reviews, totalReviews, pagingRequest.getPageNo(), pagingRequest.getSize());

        ProfileRequest memberProfile = new ProfileRequest();
        memberProfile.setMember(member);
        memberProfile.setReviews(reviewPage);
        memberProfile.setLastLogin(member.getLast_login());

        return memberProfile;
    }

    @Transactional
    //@PreAuthorize("#email == authentication.name")
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
    public void updateRoleToSeller(Long member_id) {
        memberMapper.updateSeller(member_id, Role.SELLER);
    }

    @Transactional
    //@PreAuthorize("#email == authentication.name")
    public void removeMember(Long member_id, String email) {
        memberMapper.delete(member_id);
    }

    public void sendSms(SmsRequest request) {
        String to = request.getPhone();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String certificationNumber = String.valueOf(randomNumber);
        smsService.sendSms(to, certificationNumber);
        smsConfig.createSmsCertification(to, certificationNumber);
    }

    @Transactional
    public void verifySms(SmsRequest request, HttpServletRequest httpRequest) {
        if (!isVerify(request)) {
            throw new RuntimeException("잘못된 인증입니다.");
        }
        smsConfig.removeSmsCertification(request.getPhone());

        String token = httpRequest.getHeader("Authorization").substring(7);
        Long member_id = tokenProvider.extractMemberId(token).orElseThrow(() -> new RuntimeException("Invalid Token"));

        updateRoleToSeller(member_id);
    }

    public boolean isVerify(SmsRequest request) {
        return smsConfig.hasKey(request.getPhone()) &&
                smsConfig.getSmsCertification(request.getPhone()).equals(request.getCertificationNumber());
    }
}
