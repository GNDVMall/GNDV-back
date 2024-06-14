package com.gndv.member.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.request.LoginRequest;
import com.gndv.security.token.TokenProvider;
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
public class LoginController {

    private final TokenProvider tokenProvider;

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
    public CustomResponse<LoginRequest> tokenLogin(@RequestBody LoginRequest request) {
        return CustomResponse.ok("LoginRequest", request);
    }

    @GetMapping("/v2/logout")
    @PreAuthorize("isAuthenticated()")
    public CustomResponse<Object> tokenLogout(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();

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
