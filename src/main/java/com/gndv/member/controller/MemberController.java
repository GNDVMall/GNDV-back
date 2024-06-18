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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Member API", description = "회원 관리 API")
public class MemberController {

    private final MemberService memberService;
    private final ImageService imageService;
    private final EmailService emailService;

    @PostMapping("/new")
    @Operation(summary = "회원 생성", description = "새로운 회원을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<JoinRequest> createMember(
            @RequestBody @Schema(description = "회원 가입 요청 정보") JoinRequest request) {
        log.info("JoinRequest : {}", request);
        memberService.createMember(request);
        return CustomResponse.ok("createMember", request);
    }

    @GetMapping("/{member_id}")
    @Operation(summary = "회원 조회", description = "회원 ID로 회원 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 조회 성공"),
            @ApiResponse(responseCode = "403", description = "접근 금지"),
            @ApiResponse(responseCode = "404", description = "회원 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<Optional<Member>> getMember(
            @PathVariable @Parameter(description = "회원 ID") Long member_id) {
        Optional<Member> member = memberService.getMember(member_id);
        return CustomResponse.ok("getMember", member);
    }

    @PutMapping("/{member_id}/edit/{email}")
    @Operation(summary = "회원 정보 수정", description = "회원 ID와 이메일을 사용하여 회원 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
            @ApiResponse(responseCode = "403", description = "접근 금지"),
            @ApiResponse(responseCode = "404", description = "회원 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<EditRequest> editMember(
            @PathVariable @Parameter(description = "회원 ID") Long member_id,
            @PathVariable @Parameter(description = "이메일") String email,
            @RequestBody @Schema(description = "회원 수정 요청 정보") EditRequest request) {
        memberService.editMember(member_id, email, request);
        return CustomResponse.ok("modify", request);
    }

    @PostMapping("/{member_id}/uploadProfileImage")
    @Operation(summary = "프로필 이미지 업로드", description = "회원 ID를 사용하여 프로필 이미지를 업로드합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 이미지 업로드 성공"),
            @ApiResponse(responseCode = "403", description = "접근 금지"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<String> uploadProfileImage(
            @PathVariable @Parameter(description = "회원 ID") Long member_id,
            @RequestParam @Parameter(description = "프로필 이미지 파일") MultipartFile file) throws IOException {
        String imageUrl = imageService.uploadCloud("profile", file);
        memberService.updateProfileImage(member_id, imageUrl);
        return CustomResponse.ok("Profile image uploaded", imageUrl);
    }

    @DeleteMapping("/{member_id}/delete/{email}")
    @Operation(summary = "회원 삭제", description = "회원 ID와 이메일을 사용하여 회원을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "접근 금지"),
            @ApiResponse(responseCode = "404", description = "회원 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<Object> deleteMember(
            @PathVariable @Parameter(description = "회원 ID") Long member_id,
            @PathVariable @Parameter(description = "이메일") String email) {
        memberService.removeMember(member_id, email);
        return CustomResponse.ok("deleteMemberById", null);
    }

    @PostMapping(value = "/sendEmailVerification", consumes = "multipart/form-data")
    @Operation(summary = "이메일 인증 보내기", description = "이메일 주소로 인증 이메일을 보냅니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<String> sendEmailVerification(
            @RequestParam("email") @Parameter(description = "이메일 주소") String email) {
        boolean isSent = emailService.sendEmail(email);
        if (isSent) {
            return CustomResponse.ok("Email sent successfully", null);
        } else {
            return CustomResponse.error("Failed to send email");
        }
    }

    @GetMapping("/verifyEmail")
    @Operation(summary = "이메일 인증 확인", description = "이메일과 인증 코드를 사용하여 이메일 인증을 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<String> verifyEmail(
            @RequestParam @Parameter(description = "이메일 주소") String email,
            @RequestParam @Parameter(description = "인증 코드") String code) {
        boolean isVerified = emailService.verifyCode(email, code);
        if (isVerified) {
            return CustomResponse.ok("Email verified successfully", null);
        } else {
            return CustomResponse.error("Verification failed");
        }
    }

    @PostMapping("/sms/send")
    @Operation(summary = "SMS 전송", description = "SMS 메시지를 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SMS 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<Void> sendSms(
            @RequestBody @Schema(description = "SMS 전송 요청 정보") SmsRequest request) {
        memberService.sendSms(request);
        return CustomResponse.ok("SMS sent successfully", null);
    }

    @PostMapping("/sms/confirm")
    @Operation(summary = "SMS 인증 확인", description = "SMS 인증을 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SMS 인증 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<Void> confirmSms(
            @RequestBody @Schema(description = "SMS 인증 확인 요청 정보") SmsRequest request,
            HttpServletRequest httpRequest) {
        memberService.verifySms(request, httpRequest);
        return CustomResponse.ok("SMS verified successfully, role updated to SELLER", null);
    }

    @GetMapping("/profile")
    @Operation(summary = "회원 프로필 조회", description = "이메일을 사용하여 회원 프로필을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
            @ApiResponse(responseCode = "403", description = "접근 금지"),
            @ApiResponse(responseCode = "404", description = "프로필 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CustomResponse<ProfileRequest> getMemberProfile(
            @RequestParam @Parameter(description = "이메일 주소") String email,
            @RequestParam(required = false, defaultValue = "1") @Parameter(description = "페이지 번호") int pageNo,
            @RequestParam(required = false, defaultValue = "10") @Parameter(description = "페이지 크기") int size) {
        PagingRequest pagingRequest = new PagingRequest(pageNo, size);
        ProfileRequest memberProfile = memberService.getMemberProfileWithPagedReviews(email, pagingRequest);
        return CustomResponse.ok("Member profile fetched successfully", memberProfile);
    }
}
