package com.seong.shoutlink.global.config;

import com.seong.shoutlink.domain.auth.JwtProvider;
import com.seong.shoutlink.domain.member.service.PasswordEncoder;
import com.seong.shoutlink.global.auth.authentication.AuthenticationContext;
import com.seong.shoutlink.global.auth.authentication.JwtAuthenticationProvider;
import com.seong.shoutlink.global.auth.crypto.BCryptPasswordEncoder;
import com.seong.shoutlink.global.auth.jwt.JJwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public JwtProvider jwtProvider(
        @Value("${jwt.issuer}") String issuer,
        @Value("${jwt.expiry-seconds}") int expirySeconds,
        @Value("${jwt.refresh-expiry-seconds}") int refreshExpirySeconds,
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.refresh-secret}") String refreshSecret) {
        return new JJwtProvider(issuer, expirySeconds, refreshExpirySeconds, secret, refreshSecret);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(JwtProvider jwtProvider) {
        return new JwtAuthenticationProvider(jwtProvider);
    }

    @Bean
    public AuthenticationContext authenticationContext() {
        return new AuthenticationContext();
    }
}
