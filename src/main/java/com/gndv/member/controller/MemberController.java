package com.gndv.member.controller;

import com.gndv.common.CustomResponse;
import com.gndv.image.service.ImageService;
import com.gndv.member.domain.dto.request.EditRequest;
import com.gndv.member.domain.dto.request.JoinRequest;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.service.EmailService;
import com.gndv.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public CustomResponse<Optional<Member>> getMember(@PathVariable Long member_id) {
        Optional<Member> member = memberService.getMember(member_id);
        return CustomResponse.ok("getMember", member);
    }

    @PutMapping("/{member_id}/edit/{email}")
    public CustomResponse<EditRequest> editMember(@PathVariable Long member_id, @PathVariable String email, @RequestBody EditRequest request) {
        memberService.editMember(member_id, email, request);
        return CustomResponse.ok("modify", request);
    }

    @PostMapping("/{member_id}/uploadProfileImage")
    public CustomResponse<String> uploadProfileImage(@PathVariable Long member_id, @RequestParam MultipartFile file) throws IOException {
        String imageUrl = imageService.uploadCloud("profile", file);
        memberService.updateProfileImage(member_id, imageUrl);
        return CustomResponse.ok("Profile image uploaded", imageUrl);
    }

    @DeleteMapping("/{member_id}/delete/{email}")
    public CustomResponse<Object> deleteMember(@PathVariable Long member_id, @PathVariable String email) {
        memberService.removeMember(member_id, email);
        return CustomResponse.ok("deleteMemberById", null);
    }

    @PostMapping("/sendEmailVerification")
    public CustomResponse<String> sendEmailVerification(@RequestParam String email) {
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
}
