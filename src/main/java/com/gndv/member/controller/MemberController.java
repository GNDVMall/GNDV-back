package com.gndv.member.controller;

import com.gndv.common.CustomResponse;
import com.gndv.common.domain.request.PagingRequest;
import com.gndv.common.domain.response.PageResponse;
import com.gndv.image.service.ImageService;
import com.gndv.member.domain.dto.request.*;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.service.EmailService;
import com.gndv.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Member API", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;
    private final ImageService imageService;
    private final EmailService emailService;

    @PostMapping("/new")
    @Operation(summary = "회원 가입", description = "새로운 회원을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<JoinRequest> createMember(
            @Parameter(description = "회원 가입 요청 객체", required = true) @RequestBody JoinRequest request) {
        log.info("JoinRequest : {}", request);
        memberService.createMember(request);
        return CustomResponse.ok("createMember", request);
    }

    @GetMapping("/{member_id}")
    @Operation(summary = "회원 조회", description = "회원 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<Optional<Member>> getMember(
            @Parameter(description = "회원 ID", required = true) @PathVariable Long member_id) {
        Optional<Member> member = memberService.getMember(member_id);
        return CustomResponse.ok("getMember", member);
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

    @PutMapping("/{member_id}/edit/{email}")
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "회원 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<EditRequest> editMember(
            @Parameter(description = "회원 ID", required = true) @PathVariable Long member_id,
            @Parameter(description = "이메일", required = true) @PathVariable String email,
            @Parameter(description = "회원 수정 요청 객체", required = true) @RequestBody EditRequest request) {
        memberService.editMember(member_id, email, request);
        return CustomResponse.ok("modify", request);
    }

    @PostMapping("/{member_id}/uploadProfileImage")
    @Operation(summary = "프로필 이미지 업로드", description = "회원의 프로필 이미지를 업로드합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 이미지 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<String> uploadProfileImage(
            @Parameter(description = "회원 ID", required = true) @PathVariable Long member_id,
            @Parameter(description = "프로필 이미지 파일", required = true) @RequestParam MultipartFile file) throws IOException {
        String imageUrl = imageService.uploadCloud("profile", file);
        memberService.updateProfileImage(member_id, imageUrl);

        return CustomResponse.ok("Profile image uploaded", imageUrl);
    }

    @DeleteMapping("/{member_id}/delete/{email}")
    @Operation(summary = "회원 삭제", description = "회원 정보를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "회원 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<Object> deleteMember(
            @Parameter(description = "회원 ID", required = true) @PathVariable Long member_id,
            @Parameter(description = "이메일", required = true) @PathVariable String email) {
        memberService.removeMember(member_id, email);
        return CustomResponse.ok("deleteMemberById", null);
    }

    @PostMapping(value = "/sendEmailVerification", consumes = "multipart/form-data")
    @Operation(summary = "이메일 인증 전송", description = "회원에게 이메일 인증을 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 전송 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<String> sendEmailVerification(
            @Parameter(description = "이메일 주소", required = true) @RequestParam("email") String email) {
        boolean isSent = emailService.sendEmail(email);
        if (isSent) {
            return CustomResponse.ok("Email sent successfully", null);
        } else {
            return CustomResponse.error("Failed to send email");
        }
    }

    @GetMapping("/verifyEmail")
    @Operation(summary = "이메일 인증 확인", description = "회원의 이메일 인증을 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<String> verifyEmail(
            @Parameter(description = "이메일 주소", required = true) @RequestParam String email,
            @Parameter(description = "인증 코드", required = true) @RequestParam String code) {
        boolean isVerified = emailService.verifyCode(email, code);
        if (isVerified) {
            return CustomResponse.ok("Email verified successfully", null);
        } else {
            return CustomResponse.error("Verification failed");
        }
    }

    @PostMapping("/sms/send")
    @Operation(summary = "SMS 전송", description = "회원에게 SMS를 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SMS 전송 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<Void> sendSms(
            @Parameter(description = "SMS 요청 객체", required = true) @RequestBody SmsRequest request) {
        memberService.sendSms(request);
        return CustomResponse.ok("SMS sent successfully", null);
    }

    @PostMapping("/sms/confirm")
    @Operation(summary = "SMS 인증 확인", description = "회원의 SMS 인증을 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SMS 인증 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<Void> confirmSms(
            @Parameter(description = "SMS 요청 객체", required = true) @RequestBody SmsRequest request,
            HttpServletRequest httpRequest) {
        memberService.verifySms(request, httpRequest);
        return CustomResponse.ok("SMS verified successfully, role updated to SELLER", null);
    }
}
