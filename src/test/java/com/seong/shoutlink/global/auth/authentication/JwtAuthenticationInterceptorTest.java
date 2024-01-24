package com.seong.shoutlink.global.auth.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.domain.auth.JwtProvider;
import com.seong.shoutlink.domain.auth.service.response.TokenResponse;
import com.seong.shoutlink.domain.member.MemberRole;
import com.seong.shoutlink.fixture.AuthFixture;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JwtAuthenticationInterceptorTest {

    JwtAuthenticationInterceptor jwtAuthenticationInterceptor;
    JwtAuthenticationProvider jwtAuthenticationProvider;
    JwtProvider jwtProvider;
    AuthenticationContext authenticationContext;

    @BeforeEach
    void setUp() {
        authenticationContext = new AuthenticationContext();
        jwtProvider = AuthFixture.jwtProvider();
        jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtProvider);
        jwtAuthenticationInterceptor = new JwtAuthenticationInterceptor(jwtAuthenticationProvider,
            authenticationContext);
    }

    @Nested
    @DisplayName("preHandle 메서드 호출 시")
    class PreHandleTest {

        HttpServletResponse httpServletResponse;

        @BeforeEach
        void setUp() {
            httpServletResponse = new MockHttpServletResponse();
        }

        @Test
        @DisplayName("성공: Authorization 헤더 포함 시 인증 프로세스 진행")
        void runAuthenticationProcess_WhenContainsAuthorizationHeader() {
            //given
            TokenResponse tokenResponse = jwtProvider.createToken(1L, MemberRole.ROLE_USER);
            String bearerAccessToken = "Bearer " + tokenResponse.accessToken();
            MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
            mockHttpServletRequest.addHeader("Authorization", bearerAccessToken);

            //when
            boolean result = jwtAuthenticationInterceptor.preHandle(mockHttpServletRequest,
                httpServletResponse,
                null);

            //then
            assertThat(result).isTrue();
            Authentication authentication = authenticationContext.getAuthentication();
            assertThat(authentication).isNotNull();
        }

        @Test
        @DisplayName("성공: Authorization 헤더 미 포함 시 무시")
        void ignoreAuthenticationProcess_WhenNotContainsAuthorizationHeader() {
            //given
            MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

            //when
            boolean result = jwtAuthenticationInterceptor.preHandle(mockHttpServletRequest,
                httpServletResponse,
                null);

            //then
            assertThat(result).isTrue();
            Authentication authentication = authenticationContext.getAuthentication();
            assertThat(authentication).isNull();
        }

        @Test
        @DisplayName("예외(invalidAccessToken): 액세스 토큰 형식이 Beaerer 타입이 아닐 시")
        void invalidAccessToken_WhenAccessTokenTypeIsNotBearer() {
            //given
            TokenResponse tokenResponse = jwtProvider.createToken(1L, MemberRole.ROLE_USER);
            String bearerAccessToken = "invalid" + tokenResponse.accessToken();
            MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
            mockHttpServletRequest.addHeader("Authorization", bearerAccessToken);

            //when
            Exception exception = catchException(
                () -> jwtAuthenticationInterceptor.preHandle(mockHttpServletRequest,
                    httpServletResponse,
                    null));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    @Nested
    @DisplayName("afterCompletion 메서드 호출 시")
    class AfterCompletionTest {

        HttpServletResponse httpServletResponse;

        @BeforeEach
        void setUp() {
            httpServletResponse = new MockHttpServletResponse();
        }

        @Test
        @DisplayName("성공: 인증 컨텍스트 소멸됨")
        void afterCompletion() {
            //given
            TokenResponse tokenResponse = jwtProvider.createToken(1L, MemberRole.ROLE_USER);
            String bearerAccessToken = "Bearer " + tokenResponse.accessToken();
            MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
            mockHttpServletRequest.addHeader("Authorization", bearerAccessToken);
            jwtAuthenticationInterceptor.preHandle(mockHttpServletRequest, httpServletResponse,
                null);

            //when
            jwtAuthenticationInterceptor.afterCompletion(mockHttpServletRequest,
                httpServletResponse, null, null);

            //then
            Authentication authentication = authenticationContext.getAuthentication();
            assertThat(authentication).isNull();
        }
    }
}
