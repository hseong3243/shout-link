package com.seong.shoutlink.global.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.domain.auth.JwtProvider;
import com.seong.shoutlink.domain.auth.service.response.ClaimsResponse;
import com.seong.shoutlink.domain.auth.service.response.TokenResponse;
import com.seong.shoutlink.domain.member.MemberRole;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JJwtProviderTest {

    String issuer;
    int expirySeconds;
    int refreshExpirySeconds;
    String secret;
    String refreshSecret;
    JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        issuer = "test";
        expirySeconds = 3600;
        refreshExpirySeconds = 18000;
        secret = "thisisjusttestaccesssecretsodontworry";
        refreshSecret = "thisisjusttestrefreshsecretsodontworry";
        jwtProvider
            = new JJwtProvider(issuer, expirySeconds, refreshExpirySeconds, secret, refreshSecret);
    }

    @Nested
    @DisplayName("createToken 메서드 실행 시")
    class CreateTokenTest {

        @Test
        @DisplayName("성공")
        void createToken() {
            //given
            Long memberId = 1L;
            MemberRole volunteerRole = MemberRole.ROLE_USER;

            //when
            TokenResponse TokenResponse = jwtProvider.createToken(memberId, volunteerRole);

            //then
            assertThat(TokenResponse.accessToken()).isNotBlank();
            assertThat(TokenResponse.refreshToken()).isNotBlank();
            assertThat(TokenResponse.accessToken()).isNotEqualTo(TokenResponse.refreshToken());
        }
    }

    @Nested
    @DisplayName("parseAccessToken 메서드 실행 시")
    class ParseAccessTokenTest {

        Long memberId;
        MemberRole memberRole;

        @BeforeEach
        void setUp() {
            memberId = 1L;
            memberRole = MemberRole.ROLE_USER;
        }

        @Test
        @DisplayName("성공: 액세스 토큰 파싱 결과 반환")
        void parseAccessToken() {
            //given
            TokenResponse tokenResponse = jwtProvider.createToken(memberId, memberRole);

            //when
            ClaimsResponse claims = jwtProvider.parseAccessToken(tokenResponse.accessToken());

            //then
            Long findMemberId = claims.memberId();
            List<String> findAuthorities = claims.authorities();
            assertThat(findMemberId).isEqualTo(memberId);
            assertThat(findAuthorities).containsExactlyElementsOf(memberRole.getAuthorities());
        }

        @Test
        @DisplayName("예외(invalidAccessToken): 유효하지 않은 액세스 토큰")
        void invalidAccessToken_WhenAccessTokenIsInvalid() {
            //given
            String notEqualSecret = secret + "asdf";
            JJwtProvider invalidJJwtProvider = new JJwtProvider(issuer, expirySeconds,
                refreshExpirySeconds, notEqualSecret, refreshSecret);
            TokenResponse TokenResponse = invalidJJwtProvider.createToken(memberId, memberRole);
            String invalidAccessToken = TokenResponse.accessToken();

            //when
            Exception exception = catchException(
                () -> jwtProvider.parseAccessToken(invalidAccessToken));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        @Test
        @DisplayName("예외(expiredAccessToken): 만료된 토큰")
        void expiredAccessToken_WhenAccessTokenIsExpired() {
            //given
            int alreadyExpiredSeconds = -1;
            JJwtProvider expiredJJwtProvider = new JJwtProvider(issuer, alreadyExpiredSeconds,
                refreshExpirySeconds, secret, refreshSecret);
            TokenResponse tokenResponse = expiredJJwtProvider.createToken(memberId, memberRole);
            String expiredAccessToken = tokenResponse.accessToken();

            //when
            Exception exception = catchException(
                () -> jwtProvider.parseAccessToken(expiredAccessToken));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.EXPIRED_ACCESS_TOKEN);
        }
    }
}