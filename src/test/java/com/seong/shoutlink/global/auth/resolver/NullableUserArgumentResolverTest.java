package com.seong.shoutlink.global.auth.resolver;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seong.shoutlink.base.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class NullableUserArgumentResolverTest extends BaseControllerTest {

    @Nested
    @DisplayName("@NullableUser를 핸들러 메서드의 파라미터에 선언 시")
    class LoginUserTest {

        @Test
        @DisplayName("성공: 인증된 사용자의 principal을 반환")
        void returnAuthenticatedUserPrincipal() throws Exception {
            //given

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/test/nullable-user")
                .header(AUTHORIZATION, bearerAccessToken));

            //then
            resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
        }

        @Test
        @DisplayName("성공: 인증되지 않은 사용자 요청인 경우 무시")
        void ignore_WhenUnauthenticatedUserRequest() throws Exception {
            //given

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/test/nullable-user"));

            //then
            resultActions.andExpect(status().isOk());
        }
    }
}