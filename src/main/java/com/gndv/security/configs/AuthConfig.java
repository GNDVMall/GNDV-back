package com.gndv.security.configs;

import com.gndv.member.mapper.MemberMapper;
import com.gndv.security.filters.TokenAuthenticationFilter;
import com.gndv.security.token.TokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(TokenProvider tokenProvider, MemberMapper memberMapper, ModelMapper modelMapper) {
        return new TokenAuthenticationFilter(tokenProvider, memberMapper, modelMapper);
    }
}