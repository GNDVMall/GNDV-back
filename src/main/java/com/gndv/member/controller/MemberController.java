package com.gndv.member.controller;

import com.gndv.common.CustomResponse;
import com.gndv.common.domain.request.PagingRequest;
import com.gndv.common.domain.response.PageResponse;
import com.gndv.image.service.ImageService;
import com.gndv.member.domain.dto.request.*;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.service.EmailService;
import com.gndv.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final ImageService imageService;
    private final EmailService emailService;

    @PostMapping("/new")
    public CustomResponse<JoinRequest> createMember(@RequestBody JoinRequest request) {
        log.info("JoinRequest : {}", request);
        memberService.createMember(request);
        return CustomResponse.ok("createMember", request);
    }

    @GetMapping("/{member_id}")
    //@PreAuthorize("isAuthenticated()")
    public CustomResponse<Optional<Member>> getMember(@PathVariable Long member_id) {
        Optional<Member> member = memberService.getMember(member_id);
        return CustomResponse.ok("getMember", member);
    }

    @PutMapping("/{member_id}/edit/{email}")
    //@PreAuthorize("isAuthenticated()")
    public CustomResponse<EditRequest> editMember(@PathVariable Long member_id, @PathVariable String email, @RequestBody EditRequest request) {
        memberService.editMember(member_id, email, request);
        return CustomResponse.ok("modify", request);
    }

    @PostMapping("/{member_id}/uploadProfileImage")
    //@PreAuthorize("isAuthenticated()")
    public CustomResponse<String> uploadProfileImage(@PathVariable Long member_id, @RequestParam MultipartFile file) throws IOException {
        String imageUrl = imageService.uploadCloud("profile", file);
        memberService.updateProfileImage(member_id, imageUrl);

        return CustomResponse.ok("Profile image uploaded", imageUrl);
    }

//    @PutMapping("/{member_id}/edit")
//    public CustomResponse<String> editMemberProfile(@PathVariable Long member_id, @RequestBody Map<String, String> updateData) {
//        String nickname = updateData.get("nickname");
//        String introduction = updateData.get("introduction");
//        String phone = updateData.get("phone");
//        String password = updateData.get("password");
//
//        log.info("Received update data: nickname={}, introduction={}, phone={}, password={}", nickname, introduction, phone, password);
//
//        memberService.updateProfile(member_id, nickname, introduction, phone, password);
//
//        return CustomResponse.ok("Profile updated successfully");
//    }

    @DeleteMapping("/{member_id}/delete/{email}")
    //@PreAuthorize("isAuthenticated()")
    public CustomResponse<Object> deleteMember(@PathVariable Long member_id, @PathVariable String email) {
        memberService.removeMember(member_id, email);
        return CustomResponse.ok("deleteMemberById", null);
    }

    @PostMapping(value = "/sendEmailVerification", consumes = "multipart/form-data")
    public CustomResponse<String> sendEmailVerification(@RequestParam("email") String email) {
        boolean isSent = emailService.sendEmail(email);
        if (isSent) {
            return CustomResponse.ok("Email sent successfully", null);
        } else {
            return CustomResponse.error("Failed to send email");
        }
    }

    @GetMapping("/verifyEmail")
    public CustomResponse<String> verifyEmail(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = emailService.verifyCode(email, code);
        if (isVerified) {
            return CustomResponse.ok("Email verified successfully", null);
        } else {
            return CustomResponse.error("Verification failed");
        }
    }

    @PostMapping("/sms/send")
    public CustomResponse<Void> sendSms(@RequestBody SmsRequest request) {
        memberService.sendSms(request);
        return CustomResponse.ok("SMS sent successfully", null);
    }

    @PostMapping("/sms/confirm")
    public CustomResponse<Void> confirmSms(@RequestBody SmsRequest request, HttpServletRequest httpRequest) {
        memberService.verifySms(request, httpRequest);
        return CustomResponse.ok("SMS verified successfully, role updated to SELLER", null);
    }

    @GetMapping("/profile")
    public CustomResponse<ProfileRequest> getMemberProfile(
            @RequestParam String email,
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int size) {

        PagingRequest pagingRequest = new PagingRequest(pageNo, size);
        ProfileRequest memberProfile = memberService.getMemberProfileWithPagedReviews(email, pagingRequest);

        return CustomResponse.ok("Member profile fetched successfully", memberProfile);
    }
}
