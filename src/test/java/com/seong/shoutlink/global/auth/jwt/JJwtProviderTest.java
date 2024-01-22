package com.seong.shoutlink.global.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.seong.shoutlink.domain.auth.JwtProvider;
import com.seong.shoutlink.domain.auth.service.response.TokenResponse;
import com.seong.shoutlink.domain.member.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JJwtProviderTest {

    JwtProvider jwtProvider = new JJwtProvider(
        "test", 3600, 18000,
        "thisisjusttestaccesssecretsodontworry",
        "thisisjusttestrefreshsecretsodontworry");

    @Nested
    @DisplayName("createToken 메서드 실행 시")
    class CreateTokenTest {

        @Test
        @DisplayName("성공")
        void createToken() {
            //given
            Long memberId = 1L;
            MemberRole volunteerRole = MemberRole.USER;

            //when
            TokenResponse TokenResponse = jwtProvider.createToken(memberId, volunteerRole);

            //then
            assertThat(TokenResponse.accessToken()).isNotBlank();
            assertThat(TokenResponse.refreshToken()).isNotBlank();
            assertThat(TokenResponse.accessToken()).isNotEqualTo(TokenResponse.refreshToken());
        }
    }
}