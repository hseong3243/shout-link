package com.seong.shoutlink.global.auth.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seong.shoutlink.base.BaseControllerTest;
import com.seong.shoutlink.domain.auth.service.response.TokenResponse;
import com.seong.shoutlink.domain.member.MemberRole;
import com.seong.shoutlink.fixture.AuthFixture;
import com.seong.shoutlink.global.exception.ErrorCode;
import com.seong.shoutlink.global.exception.ShoutLinkException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class LoginUserArgumentResolverTest extends BaseControllerTest {

    @Nested
    @DisplayName("@LoginUser를 핸들러 메서드의 파라미터에 선언 시")
    class LoginUserTest {

        @Test
        @DisplayName("성공: 인증된 사용자의 principal을 반환")
        void returnAuthenticatedUserPrincipal() throws Exception {
            //given
            TokenResponse tokenResponse = AuthFixture.jwtProvider()
                .createToken(1L, MemberRole.ROLE_USER);
            String bearerAccessToken = "Bearer " + tokenResponse.accessToken();

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/test/login-user")
                .header(AUTHORIZATION, bearerAccessToken));

            //then
            resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
        }

        @Test
        @DisplayName("예외(unauthenticated): 인증되지 않은 사용자 요청")
        void unauthenticated_WhenUnauthenticatedUserRequest() throws Exception {
            //given
            //when
            //then
            mockMvc.perform(get("/api/test/login-user"))
                .andExpect(
                    result -> {
                        Exception exception = result.getResolvedException();
                        assertThat(exception)
                            .isInstanceOf(ShoutLinkException.class)
                            .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                            .isEqualTo(ErrorCode.UNAUTHENTICATED);
                    }
                );
        }
    }
}