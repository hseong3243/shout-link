package com.seong.shoutlink.global.auth.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import com.seong.shoutlink.domain.auth.JwtProvider;
import com.seong.shoutlink.domain.auth.service.response.TokenResponse;
import com.seong.shoutlink.domain.member.MemberRole;
import com.seong.shoutlink.global.auth.jwt.JJwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JwtAuthenticationProviderTest {

    JwtProvider jwtProvider;
    JwtAuthenticationProvider jwtAuthenticationProvider;

    @BeforeEach
    void setUp() {
        String issuer = "test";
        int expirySeconds = 3600;
        int refreshExpirySeconds = 18000;
        String secret = "thisisjusttestaccesssecretsodontworry";
        String refreshSecret = "thisisjusttestrefreshsecretsodontworry";
        jwtProvider
            = new JJwtProvider(issuer, expirySeconds, refreshExpirySeconds, secret, refreshSecret);
        jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtProvider);
    }

    @Nested
    @DisplayName("성공: authenticate 메서드 호출 시")
    class AuthenticatedTest {

        @Test
        @DisplayName("성공: 사용자 인증됨")
        void authenticate() {
            //given
            long memberId = 1L;
            MemberRole memberRole = MemberRole.ROLE_USER;
            TokenResponse tokenResponse = jwtProvider.createToken(memberId, memberRole);

            //when
            JwtAuthentication authentication = jwtAuthenticationProvider.authenticate(
                tokenResponse.accessToken());

            //then
            assertThat(authentication.getPrincipal()).isEqualTo(memberId);
            assertThat(authentication.getAuthorities()).isEqualTo(memberRole.getAuthorities());
        }
    }
}
