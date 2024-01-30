package com.seong.shoutlink.domain.linkbundle.controller;

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
import com.seong.shoutlink.domain.linkbundle.controller.request.CreateLinkBundleRequest;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class LinkBundleControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 링크 번들 생성 API 호출 시")
    void createLinkBundle() throws Exception {
        //given
        CreateLinkBundleRequest request = new CreateLinkBundleRequest("새 묶음", false);
        CreateLinkBundleResponse response = new CreateLinkBundleResponse(1L);

        given(linkBundleService.createLinkBundle(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/link-bundles")
            .header(AUTHORIZATION, bearerAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions.andExpect(status().isCreated())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("description").type(JsonFieldType.STRING).description("간단한 설명"),
                    fieldWithPath("isDefault").type(JsonFieldType.BOOLEAN).description("기본 묶음 여부")
                ),
                responseFields(
                    fieldWithPath("linkBundleId").type(JsonFieldType.NUMBER)
                        .description("생성된 링크 번들 ID")
                )
            ));
    }
}