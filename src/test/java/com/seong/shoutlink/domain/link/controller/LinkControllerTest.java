package com.seong.shoutlink.domain.link.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seong.shoutlink.base.BaseControllerTest;
import com.seong.shoutlink.domain.link.controller.request.CreateLinkRequest;
import com.seong.shoutlink.domain.link.service.response.CreateLinkResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class LinkControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 링크 생성 API 호출 시")
    void createLink() throws Exception {
        //given
        CreateLinkRequest request = new CreateLinkRequest(1L, "https://hseong.tistory.com/", "내 블로그");
        CreateLinkResponse response = new CreateLinkResponse(1L);
        given(linkService.createLink(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/links")
            .header(AUTHORIZATION, bearerAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions.andExpect(status().isCreated())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("사용자 액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("linkBundleId").type(JsonFieldType.NUMBER).description("링크 번들 ID"),
                    fieldWithPath("url").type(JsonFieldType.STRING).description("링크 url"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("링크 설명")
                        .optional()
                ),
                responseFields(
                    fieldWithPath("linkId").type(JsonFieldType.NUMBER).description("생성된 링크 ID")
                )
            ));
    }
}