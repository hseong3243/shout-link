package com.seong.shoutlink.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seong.shoutlink.base.BaseControllerTest;
import com.seong.shoutlink.domain.auth.controller.request.CreateMemberRequest;
import com.seong.shoutlink.domain.auth.controller.request.LoginRequest;
import com.seong.shoutlink.domain.auth.service.response.CreateMemberResponse;
import com.seong.shoutlink.domain.auth.service.response.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class AuthControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 회원 등록 api 호출")
    void createMember() throws Exception {
        //given
        CreateMemberRequest createMemberRequest = new CreateMemberRequest("email@email.com",
            "asdf1234!", "닉네임");
        CreateMemberResponse createMemberResponse = new CreateMemberResponse(1L);

        given(authService.createMember(any())).willReturn(createMemberResponse);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createMemberRequest)));

        //then
        resultActions.andExpect(status().isCreated())
            .andDo(restDocs.document(
                requestFields(
                    fieldWithPath("email").type(STRING).description("사용자 이메일"),
                    fieldWithPath("password").type(STRING).description("사용자 비밀번호"),
                    fieldWithPath("nickname").type(STRING).description("사용자 닉네임")
                ),
                responseFields(
                    fieldWithPath("memberId").type(NUMBER).description("사용자 ID")
                )
            ));
    }
    @Test
    @DisplayName("성공: 로그인 api 호출")
    void login() throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest("email@email.com", "asdf123!");
        LoginResponse loginResponse = new LoginResponse(1L, "accessToken", "refreshToken");

        given(authService.login(any())).willReturn(loginResponse);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestFields(
                    fieldWithPath("email").type(STRING).description("사용자 이메일"),
                    fieldWithPath("password").type(STRING).description("사용자 비밀번호")
                ),
                responseCookies(
                    cookieWithName("refreshToken").description("리프레시 토큰 쿠키")
                ),
                responseFields(
                    fieldWithPath("memberId").type(NUMBER).description("사용자 ID"),
                    fieldWithPath("accessToken").type(STRING).description("액세스 토큰")
                )
            ));
    }
}