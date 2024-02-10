package com.seong.shoutlink.domain.hub.controller;

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
import com.seong.shoutlink.domain.hub.controller.request.CreateHubRequest;
import com.seong.shoutlink.domain.hub.service.response.CreateHubResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class HubControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 허브 생성 api 호출 시")
    void createHub() throws Exception {
        //given
        CreateHubRequest request = new CreateHubRequest("허브 이름", "허브 설명");
        CreateHubResponse response = new CreateHubResponse(1L);

        given(hubService.createHub(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/hubs")
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
                    fieldWithPath("name").type(JsonFieldType.STRING).description("허브 이름"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("허브 설명")
                        .optional()
                ),
                responseFields(
                    fieldWithPath("hubId").type(JsonFieldType.NUMBER).description("허브 ID")
                )
            ));
    }
}