package com.gndv.member.service;

import com.gndv.common.domain.request.PagingRequest;
import com.gndv.common.domain.response.PageResponse;
import com.gndv.configs.SmsConfig;
import com.gndv.constant.Role;
import com.gndv.member.domain.dto.request.EditRequest;
import com.gndv.member.domain.dto.request.JoinRequest;
import com.gndv.member.domain.dto.request.ProfileRequest;
import com.gndv.member.domain.dto.request.SmsRequest;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.review.domain.entity.Review;
import com.gndv.review.mapper.ReviewMapper;
import com.gndv.security.token.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    private final ReviewMapper reviewMapper;
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
        Long memberId = tokenProvider.extractMemberId(token).orElseThrow(() -> new RuntimeException("Invalid Token"));

        updateRoleToSeller(memberId);
    }

    public boolean isVerify(SmsRequest request) {
        return smsConfig.hasKey(request.getPhone()) &&
                smsConfig.getSmsCertification(request.getPhone()).equals(request.getCertificationNumber());
    }

    @Transactional
    public void updateRoleToSeller(Long memberId) {
        memberMapper.updateSeller(memberId, Role.SELLER);
    }

    @Transactional(readOnly = true)
    public ProfileRequest getMemberProfile(String email, PagingRequest pagingRequest) {
        Member member = memberMapper.findByEmail(email).orElseThrow(() -> new RuntimeException("Member not found"));

        List<Review> reviews = reviewMapper.findReviewsByEmail(email, pagingRequest.getSkip(), pagingRequest.getSize());
        int totalReviews = reviewMapper.countReviewsByEmail(email);

        PageResponse<Review> reviewPage = new PageResponse<>(reviews, totalReviews, pagingRequest.getPageNo(), pagingRequest.getSize());

        ProfileRequest memberProfile = new ProfileRequest();
        memberProfile.setMember(member);
        memberProfile.setReviews(reviewPage.getList());

        return memberProfile;
    }

    @Transactional
    public void updateProfile(Long memberId, String nickname, String introduction) {
        Member member = memberMapper.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Member updatedMember = Member.builder()
                .member_id(member.getMember_id())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(nickname != null ? nickname : member.getNickname())
                .phone(member.getPhone())
                .introduction(introduction != null ? introduction : member.getIntroduction())
                .profile_url(member.getProfile_url())
                .created_at(member.getCreated_at())
                .rating(member.getRating())
                .report_count(member.getReport_count())
                .last_login(member.getLast_login())
                .role(member.getRole())
                .member_status(member.getMember_status())
                .is_account_non_expired(member.is_account_non_expired())
                .is_account_non_locked(member.is_account_non_locked())
                .is_credentials_non_expired(member.is_credentials_non_expired())
                .is_enabled(member.is_enabled())
                .build();

        memberMapper.update(updatedMember.getMember_id(), updatedMember.getEmail(), updatedMember.getPassword(),
                updatedMember.getNickname(), updatedMember.getPhone(), updatedMember.getIntroduction());
    }
}
