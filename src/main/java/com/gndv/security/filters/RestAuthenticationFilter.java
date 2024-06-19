package com.gndv.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gndv.common.WebUtil;
import com.gndv.member.domain.dto.MemberDTO;
import com.gndv.security.token.RestAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RestAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/login", "POST"));
    }

    public SecurityContextRepository getSecurityContextRepository(HttpSecurity http) {

        SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);

        if (securityContextRepository == null) {
            securityContextRepository = new DelegatingSecurityContextRepository(
                    new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
        }

        return securityContextRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        if (!HttpMethod.POST.name().equals(request.getMethod()) || !WebUtil.isAjax(request)) {
            throw new IllegalArgumentException("Authentication method not supported");
        }

        MemberDTO memberDTO = objectMapper.readValue(request.getReader(), MemberDTO.class);

        if (!StringUtils.hasText(memberDTO.getEmail()) || !StringUtils.hasText(memberDTO.getPassword())) {
            throw new AuthenticationServiceException("Username or Password not provided");
        }
        RestAuthenticationToken token = new RestAuthenticationToken(memberDTO.getEmail(), memberDTO.getPassword());

        return this.getAuthenticationManager().authenticate(token);
    }
}
