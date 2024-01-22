package com.seong.shoutlink.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seong.shoutlink.base.BaseControllerTest;
import com.seong.shoutlink.domain.auth.controller.request.CreateMemberRequest;
import com.seong.shoutlink.domain.auth.service.response.CreateMemberResponse;
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
}