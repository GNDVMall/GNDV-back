package com.gndv.security.filters;

import com.gndv.member.domain.dto.MemberContext;
import com.gndv.member.domain.dto.MemberDTO;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.security.service.JwtService;
import com.gndv.security.token.RestAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MemberMapper memberMapper;
    private final ModelMapper modelMapper;

    private final String NO_CHECK_URL = "/api/v2/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService
                .extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractEmail(accessToken)
                        .ifPresent(email -> memberMapper.findByEmail(email)
                                .ifPresent(member -> saveAuthentication(member)
                        )
                )
        );

        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(Member member) {
        MemberContext userDetails = new MemberContext(modelMapper.map(member, MemberDTO.class), null);

        RestAuthenticationToken authentication = new RestAuthenticationToken(userDetails, null);

        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authentication);
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        memberMapper.findByRefreshToken(refreshToken)
                .ifPresent(member -> {
                    String newAccessToken = jwtService.createAccessToken(member.getEmail());
                    jwtService.sendAccessToken(response, newAccessToken);

                    if (jwtService.isTokenCloseToExpiry(refreshToken)) {
                        String newRefreshToken = jwtService.createRefreshToken();
                        jwtService.updateRefreshToken(member.getEmail(), newRefreshToken);
                        jwtService.sendRefreshToken(response, newRefreshToken);
                    }
                });
    }
}
