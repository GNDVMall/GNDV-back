package com.gndv.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gndv.member.domain.dto.MemberContext;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.security.token.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("jwtSuccessHandler")
@RequiredArgsConstructor
public class TokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final MemberMapper memberMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        MemberContext memberContext = (MemberContext) authentication.getPrincipal();
        String email = memberContext.getMemberDTO().getEmail();
        Long member_id = memberContext.getMemberDTO().getMember_id();

        memberMapper.updateLastLogin(member_id, new Date());

        String accessToken = tokenProvider.createAccessToken(email, member_id);
        String refreshToken = tokenProvider.createRefreshToken();

        tokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        memberMapper.findByEmail(email).ifPresent(
                member -> member.updateRefreshToken(refreshToken)
        );

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // JSON 객체 생성
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("status", "success");
        jsonResponse.put("email", email);
        jsonResponse.put("member_id", member_id);

        // JSON 형식으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponseString = objectMapper.writeValueAsString(jsonResponse);
        response.getWriter().write(jsonResponseString);

        clearAuthenticationAttributes(request);
    }
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
