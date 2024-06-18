package com.gndv.member.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.request.LoginRequest;
import com.gndv.security.token.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Login API", description = "로그인 및 로그아웃 API")
public class LoginController {

    private final TokenProvider tokenProvider;

    @PostMapping("/v1/login")
    @Operation(summary = "세션 로그인", description = "세션을 사용하여 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @ResponseBody
    public CustomResponse<LoginRequest> sessionLogin(
            @Parameter(description = "로그인 요청 객체", required = true) @RequestBody LoginRequest request) {
        return CustomResponse.ok("LoginRequest", request);
    }

    @GetMapping("/v1/logout")
    @Operation(summary = "세션 로그아웃", description = "세션을 사용하여 로그아웃합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    public CustomResponse<Object> sessionLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        log.info("authentication: {}", authentication);
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return CustomResponse.ok("logout", null);
    }

    @PostMapping("/v2/login")
    @Operation(summary = "토큰 로그인", description = "JWT 토큰을 사용하여 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @ResponseBody
    public CustomResponse<LoginRequest> tokenLogin(
            @Parameter(description = "로그인 요청 객체", required = true) @RequestBody LoginRequest request) {
        return CustomResponse.ok("LoginRequest", request);
    }

    @GetMapping("/v2/logout")
    @Operation(summary = "토큰 로그아웃", description = "JWT 토큰을 사용하여 로그아웃합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    //@PreAuthorize("isAuthenticated()")
    public CustomResponse<Object> tokenLogout(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();

        System.out.println(authentication);
        log.info("authentication: {}", authentication);

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            String refreshToken = tokenProvider.extractRefreshToken(request).orElse(null);
            if (refreshToken != null) {
                String email = tokenProvider.extractEmail(refreshToken).orElse(null);
                if (email != null) {
                    tokenProvider.destroyRefreshToken(email);
                }
            }
        }

        return CustomResponse.ok("logout", authentication);
    }
}
