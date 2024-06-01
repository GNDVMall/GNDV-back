package com.gndv.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gndv.member.domain.dto.MemberContext;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.security.Util.JwtUtil;
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

@Component("restSuccessHandler")
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        MemberContext memberContext = (MemberContext) authentication.getPrincipal();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        memberContext.getMemberDTO().setPassword(null);
        mapper.writeValue(response.getWriter(), memberContext.getMemberDTO());

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
