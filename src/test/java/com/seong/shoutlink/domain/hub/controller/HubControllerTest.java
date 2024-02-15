package com.seong.shoutlink.domain.hub.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seong.shoutlink.base.BaseControllerTest;
import com.seong.shoutlink.domain.hub.controller.request.CreateHubRequest;
import com.seong.shoutlink.domain.hub.service.response.CreateHubResponse;
import com.seong.shoutlink.domain.hub.service.response.FindHubResponse;
import com.seong.shoutlink.domain.hub.service.response.FindHubsResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class HubControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 허브 생성 api 호출 시")
    void createHub() throws Exception {
        //given
        CreateHubRequest request = new CreateHubRequest("허브 이름", "허브 설명", false);
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
                        .optional(),
                    fieldWithPath("isPrivate").type(JsonFieldType.BOOLEAN).description("허브 공개 여부r")
                ),
                responseFields(
                    fieldWithPath("hubId").type(JsonFieldType.NUMBER).description("허브 ID")
                )
            ));
    }

    @Test
    @DisplayName("성공: 허브 목록 조회 api 호출 시")
    void findHubs() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("size", "0");
        FindHubResponse content = new FindHubResponse(1L, 1L, "허브 이름", "설명", false);
        FindHubsResponse response = new FindHubsResponse(List.of(content), 1, false);

        given(hubService.findHubs(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/hubs")
            .params(params));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("page").description("페이지").optional(),
                    parameterWithName("size").description("사이즈").optional()
                ),
                responseFields(
                    fieldWithPath("hubs").type(JsonFieldType.ARRAY).description("허브 목록"),
                    fieldWithPath("hubs[].hubId").type(JsonFieldType.NUMBER).description("허브 ID"),
                    fieldWithPath("hubs[].masterId").type(JsonFieldType.NUMBER)
                        .description("사용자 ID"),
                    fieldWithPath("hubs[].name").type(JsonFieldType.STRING).description("허브 이름"),
                    fieldWithPath("hubs[].description").type(JsonFieldType.STRING)
                        .description("허브 설명"),
                    fieldWithPath("hubs[].isPrivate").type(JsonFieldType.BOOLEAN)
                        .description("허브 공개 여부"),
                    fieldWithPath("totalElements").type(JsonFieldType.NUMBER)
                        .description("총 요소 개수"),
                    fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 여부")
                )
            ));
    }
}