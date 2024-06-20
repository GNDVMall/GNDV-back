package com.gndv.security.configs;

import com.gndv.security.dsl.RestApiDsl;
import com.gndv.security.entrypoint.RestAuthenticationEntryPoint;
import com.gndv.security.filters.TokenAuthenticationFilter;
import com.gndv.security.handler.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationProvider restAuthenticationProvider;
    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
    private final FormAuthenticationSuccessHandler successHandler;
    private final FormAuthenticationFailureHandler failureHandler;
    private final RestAuthenticationSuccessHandler restSuccessHandler;
    private final RestAuthenticationFailureHandler restFailureHandler;
    private final TokenAuthenticationSuccessHandler tokenSuccessHandler;
    private final TokenAuthenticationFailureHandler tokenFailureHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login")
                        .authenticationDetailsSource(authenticationDetailsSource)
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll())
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new FormAccessDeniedHandler("/denied"))
                )
        ;
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(restAuthenticationProvider);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .securityMatcher("/api/v1/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationManager(authenticationManager)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(new RestAccessDeniedHandler()))
                .with(new RestApiDsl<>(), restDsl -> restDsl
                        .restSuccessHandler(restSuccessHandler)
                        .restFailureHandler(restFailureHandler)
                        .loginPage("/api/login")
                        .loginProcessingUrl("/api/login"));

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain tokenSecurityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(restAuthenticationProvider);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .securityMatcher("/api/v2/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v2/members/new", "/api/v2/login", "/api/v2/items","/api/v2/items/*"
                                ,"/api/v2/products/*","/api/v2/order/*","/api/v2/order/payment/*","/api/v2/order","/api/v2/order/payment",
                                "/api/v2/payment/","/api/v2/payment/*" ,"/api/v2/chat/**", "/api/v2/chat", "/api/v2/search**", "/api/v2/code/**", "/api/v2/**","/api/v2/recent-product").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v2/products").permitAll()
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationManager(authenticationManager)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(new TokenAccessDeniedHandler()));
        http
                .addFilterBefore(new CorsFilter(corsConfigurationSource()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .with(new RestApiDsl<>(), restDsl -> restDsl
                        .restSuccessHandler(tokenSuccessHandler)
                        .restFailureHandler(tokenFailureHandler)
                        .loginPage("/api/v2/login")
                        .loginProcessingUrl("/api/v2/login"));

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://175.45.203.235:5173", "http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "access", "refresh"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "access", "refresh"));
        configuration.setMaxAge(3600L); // 1시간

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
