package com.gndv.chat.handler;

import com.gndv.member.domain.dto.MemberContext;
import com.gndv.member.domain.dto.MemberDTO;
import com.gndv.member.domain.entity.Member;
import com.gndv.member.mapper.MemberMapper;
import com.gndv.security.token.RestAuthenticationToken;
import com.gndv.security.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    private final TokenProvider tokenProvider;
    private final MemberMapper memberMapper;
    private final ModelMapper modelMapper;


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String jwtToken = getTokenFromRequest(request);
        if (jwtToken != null && tokenProvider.isTokenValid(jwtToken)) {
            tokenProvider.extractEmail(jwtToken)
                    .ifPresent(email -> memberMapper.findByEmail(email)
                            .ifPresent(member -> {
                                        saveAuthentication(member);
                                        attributes.put("member", member);
                                    }
                            )
                    );
            return true;
        }
        return false;
    }

    private String getTokenFromRequest(ServerHttpRequest request) {
        // URL 쿼리 매개변수에서 JWT 토큰을 추출합니다.
        return UriComponentsBuilder.fromUri(request.getURI())
                .build()
                .getQueryParams()
                .getFirst("token");
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    private void saveAuthentication(Member member) {
        MemberContext userDetails = new MemberContext(modelMapper.map(member, MemberDTO.class), null);
        RestAuthenticationToken authentication = new RestAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authentication);
    }
}
