package com.seong.shoutlink.global.auth.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import com.seong.shoutlink.domain.member.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AuthenticationContextTest {

    AuthenticationContext authenticationContext = new AuthenticationContext();

    @Nested
    @DisplayName("setAuthentication 메서드 호출 시")
    class SetAuthenticationTest {

        @Test
        @DisplayName("성공: 인증 컨텍스트 사용자 인증 정보 설정됨")
        void setAuthentication() {
            //given
            long memberId = 1L;
            MemberRole memberRole = MemberRole.ROLE_USER;
            String accessToken = "accessToken";
            JwtAuthentication givenAuthentication
                = new JwtAuthentication(memberId, memberRole, accessToken);

            //when
            authenticationContext.setAuthentication(givenAuthentication);

            //then
            Authentication authentication = authenticationContext.getAuthentication();
            assertThat(authentication.getPrincipal()).isEqualTo(memberId);
            assertThat(authentication.getCredentials()).isEqualTo(accessToken);
            assertThat(authentication.getAuthorities())
                .containsExactlyElementsOf(memberRole.getAuthorities());
        }
    }

    @Nested
    @DisplayName("releaseContext 메서드 호출 시")
    class ReleaseContextTest {

        @Test
        @DisplayName("성공: 인증 컨텍스트 제거됨")
        void releaseContext() {
            //given
            JwtAuthentication givenAuthentication
                = new JwtAuthentication(1L, MemberRole.ROLE_USER, "accessToken");
            authenticationContext.setAuthentication(givenAuthentication);

            //when
            authenticationContext.releaseContext();

            //then
            assertThat(authenticationContext)
                .extracting(AuthenticationContext::getAuthentication)
                .isNull();
        }
    }
}