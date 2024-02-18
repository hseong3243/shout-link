package com.seong.shoutlink.domain.linkbundle.controller;

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
import com.seong.shoutlink.domain.linkbundle.controller.request.CreateLinkBundleRequest;
import com.seong.shoutlink.domain.linkbundle.service.response.CreateLinkBundleResponse;
import com.seong.shoutlink.domain.linkbundle.service.response.FindLinkBundleResponse;
import com.seong.shoutlink.domain.linkbundle.service.response.FindLinkBundlesResponse;
import java.util.List;
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

    @Test
    @DisplayName("성공: 링크 묶음 목록 조회 API 호출 시")
    void findLinkBundles() throws Exception {
        //given
        FindLinkBundlesResponse response = new FindLinkBundlesResponse(List.of(
            new FindLinkBundleResponse(1L, "기본 분류", true),
            new FindLinkBundleResponse(2L, "두번째", false)));
        given(linkBundleService.findLinkBundles(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/link-bundles")
            .header(AUTHORIZATION, bearerAccessToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("액세스 토큰")
                ),
                responseFields(
                    fieldWithPath("linkBundles").type(JsonFieldType.ARRAY).description("링크 묶음 목록"),
                    fieldWithPath("linkBundles[].linkBundleId").type(JsonFieldType.NUMBER)
                        .description("링크 묶음 ID"),
                    fieldWithPath("linkBundles[].description").type(JsonFieldType.STRING)
                        .description("설명"),
                    fieldWithPath("linkBundles[].isDefault").type(JsonFieldType.BOOLEAN)
                        .description("기본 여부")
                )
            ));
    }

    @Test
    @DisplayName("성공: 허브 링크 묶음 생성 API 호출 시")
    void createHubLinkBundle() throws Exception {
        //given
        CreateLinkBundleRequest request = new CreateLinkBundleRequest("설명", false);
        Long hubId = 1L;
        CreateLinkBundleResponse response = new CreateLinkBundleResponse(1L);

        given(linkBundleService.createHubLinkBundle(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/hubs/{hubId}/link-bundles", hubId)
            .header(AUTHORIZATION, bearerAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions.andExpect(status().isCreated())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("hubId").description("허브 ID")
                ),
                requestFields(
                    fieldWithPath("description").type(JsonFieldType.STRING).description("설명"),
                    fieldWithPath("isDefault").type(JsonFieldType.BOOLEAN).description("기본 여부")
                ),
                responseFields(
                    fieldWithPath("linkBundleId").type(JsonFieldType.NUMBER).description("링크 묶음 ID")
                )
            ));
    }

    @Test
    @DisplayName("성공: 허브 링크 묶음 목록 조회 api 호출 시")
    void findHubLinkBundles() throws Exception {
        //given
        long hubId = 1L;
        FindLinkBundleResponse content = new FindLinkBundleResponse(1L, "기본", false);
        FindLinkBundlesResponse response = new FindLinkBundlesResponse(List.of(content));

        given(linkBundleService.findHubLinkBundles(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/hubs/{hubId}/link-bundles", hubId)
            .header(AUTHORIZATION, bearerAccessToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("hubId").description("허브 ID")
                ),
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("액세스 토큰").optional()
                ),
                responseFields(
                    fieldWithPath("linkBundles").type(JsonFieldType.ARRAY).description("링크 묶음 목록"),
                    fieldWithPath("linkBundles[].linkBundleId").type(JsonFieldType.NUMBER)
                        .description("링크 묶음 ID"),
                    fieldWithPath("linkBundles[].description").type(JsonFieldType.STRING)
                        .description("링크 묶음 설명"),
                    fieldWithPath("linkBundles[].isDefault").type(JsonFieldType.BOOLEAN)
                        .description("기본 여부")
                )
            ));
    }
}