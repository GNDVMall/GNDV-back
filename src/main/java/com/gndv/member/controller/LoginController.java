package com.gndv.member.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.LoginRequest;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final JwtService jwtService;
    private final MemberMapper memberMapper;

    @PostMapping("/v1/login")
    @ResponseBody
    public CustomResponse<LoginRequest> sessionLogin(@RequestBody LoginRequest request) {
        return CustomResponse.ok("LoginRequest", request);
    }

    @GetMapping("/v1/logout")
    public CustomResponse<Object> sessionLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        log.info("authentication: {}", authentication);
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return CustomResponse.ok("logout", null);
    }

    @PostMapping("/v2/login")
    @ResponseBody
    public CustomResponse<Map<String, Object>> tokenLogin(@RequestBody LoginRequest request, HttpServletResponse response) {
        // 사용자 인증 로직 수행
        Member member = memberMapper.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        Long memberId = member.getMember_id(); // 실제 인증 로직에서 가져온 memberId
        String accessToken = jwtService.createAccessToken(request.getEmail(), memberId);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        // 응답에 memberId를 포함시켜 반환
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("email", request.getEmail());
        responseMap.put("member_id", memberId);
        responseMap.put("accessToken", accessToken);
        responseMap.put("refreshToken", refreshToken);

        return CustomResponse.ok("LoginRequest", responseMap);
    }

    @GetMapping("/v2/logout")
    public CustomResponse<Object> tokenLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        log.info("authentication: {}", authentication);
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            String refreshToken = jwtService.extractRefreshToken(request).orElse(null);
            if (refreshToken != null) {
                String email = jwtService.extractEmail(refreshToken).orElse(null);
                if (email != null) {
                    jwtService.destroyRefreshToken(email);
                }
            }
        }

        return CustomResponse.ok("logout", authentication);
    }
}
