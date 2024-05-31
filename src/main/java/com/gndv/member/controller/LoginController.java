package com.gndv.member.controller;

import com.gndv.common.CustomResponse;
import com.gndv.member.domain.dto.LoginRequest;
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
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    @PostMapping("/api/v1/login")
    @ResponseBody
    public CustomResponse<LoginRequest> sessionLogin(@RequestBody LoginRequest request) {
        return CustomResponse.ok("LoginRequest", request);
    }

    @GetMapping("/api/v1/logout")
    @PreAuthorize("isAuthenticated()")
    public CustomResponse<Object> sessionLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        log.info("authentication: {}", authentication);
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return CustomResponse.ok("logout", null);
    }

    @PostMapping("/api/v2/login")
    @ResponseBody
    public CustomResponse<LoginRequest> tokenLogin(@RequestBody LoginRequest request) {
        return CustomResponse.ok("LoginRequest", request);
    }

    @GetMapping("/api/v2/logout")
    @PreAuthorize("isAuthenticated()")
    public CustomResponse<Object> tokenLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        log.info("authentication: {}", authentication);
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return CustomResponse.ok("logout", null);
    }
}
