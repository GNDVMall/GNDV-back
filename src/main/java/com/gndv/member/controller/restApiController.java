package com.gndv.member.controller;

import com.gndv.member.domain.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class restApiController {

    @GetMapping(value="/user")
    public MemberDTO restUser(@AuthenticationPrincipal MemberDTO memberDTO) {
        return memberDTO;
    }

    @GetMapping(value="/seller")
    public MemberDTO restManager(@AuthenticationPrincipal MemberDTO memberDTO) {
        return memberDTO;
    }

    @GetMapping(value="/admin")
    public MemberDTO restAdmin(@AuthenticationPrincipal MemberDTO memberDTO) {
        return memberDTO;
    }

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "logout";
    }
}
