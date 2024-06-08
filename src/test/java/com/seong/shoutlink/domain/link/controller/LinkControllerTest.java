package com.seong.shoutlink.domain.link.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seong.shoutlink.base.BaseControllerTest;
import com.seong.shoutlink.domain.link.link.controller.request.CreateLinkRequest;
import com.seong.shoutlink.domain.link.link.service.response.CreateHubLinkResponse;
import com.seong.shoutlink.domain.link.link.service.response.CreateLinkResponse;
import com.seong.shoutlink.domain.link.link.service.response.DeleteLinkResponse;
import com.seong.shoutlink.domain.link.link.service.response.FindLinkResponse;
import com.seong.shoutlink.domain.link.link.service.response.FindLinksResponse;
import com.seong.shoutlink.fixture.LinkFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class LinkControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 링크 생성 API 호출 시")
    void createLink() throws Exception {
        //given
        CreateLinkRequest request = new CreateLinkRequest(1L, "https://hseong.tistory.com/",
            "내 블로그", LocalDateTime.of(2050, 10, 10, 10, 10));
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
                    fieldWithPath("linkBundleId").type(JsonFieldType.NUMBER)
                        .description("링크 묶음 ID"),
                    fieldWithPath("url").type(JsonFieldType.STRING).description("링크 url"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("링크 설명")
                        .optional(),
                    fieldWithPath("expiredAt").type(JsonFieldType.STRING).description("만료일")
                ),
                responseFields(
                    fieldWithPath("linkId").type(JsonFieldType.NUMBER).description("생성된 링크 ID")
                )
            ));
    }

    @Test
    @DisplayName("성공: 링크 목록 조회 API 호출 시")
    void findLinks() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("linkBundleId", "1");
        params.add("page", "0");
        params.add("size", "10");
        params.add("linkOrderBy", "CREATED_AT");
        FindLinkResponse findLinkResponse = new FindLinkResponse(1L, "url", "간단한 설명",
            LinkFixture.DEFAULT_EXPIRED_AT);
        FindLinksResponse response = new FindLinksResponse(List.of(findLinkResponse), 1,
            false);

        given(linkService.findLinks(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/links")
            .header(AUTHORIZATION, bearerAccessToken)
            .params(params));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("액세스 토큰")
                ),
                queryParameters(
                    parameterWithName("linkBundleId").description("링크 묶음 ID"),
                    parameterWithName("page").description("페이지"),
                    parameterWithName("size").description("사이즈"),
                    parameterWithName("linkOrderBy").description("정렬 기준")
                ),
                responseFields(
                    fieldWithPath("links").type(JsonFieldType.ARRAY).description("링크 목록"),
                    fieldWithPath("links[].linkId").type(JsonFieldType.NUMBER).description("링크 ID"),
                    fieldWithPath("links[].url").type(JsonFieldType.STRING).description("링크 url"),
                    fieldWithPath("links[].description").type(JsonFieldType.STRING)
                        .description("링크 설명"),
                    fieldWithPath("links[].expiredAt").type(JsonFieldType.STRING).description("링크 만료일"),
                    fieldWithPath("totalElements").type(JsonFieldType.NUMBER)
                        .description("총 요소 개수"),
                    fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 여부")
                )
            ));
    }

    @Test
    @DisplayName("성공: 허브 링크 생성 api 호출 시")
    void createHubLink() throws Exception {
        //given
        Long hubId = 1L;
        CreateLinkRequest request = new CreateLinkRequest(1L, "url", "설명",
            LocalDateTime.of(2050, 10, 10, 10, 10));

        given(linkService.createHubLink(any())).willReturn(new CreateHubLinkResponse(1L));

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/hubs/{hubId}/links", hubId)
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
                    fieldWithPath("linkBundleId").type(JsonFieldType.NUMBER)
                        .description("링크 묶음 ID"),
                    fieldWithPath("url").type(JsonFieldType.STRING).description("링크 url"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("설명"),
                    fieldWithPath("expiredAt").type(JsonFieldType.STRING).description("만료일")
                ),
                responseFields(
                    fieldWithPath("linkId").type(JsonFieldType.NUMBER).description("링크 ID")
                )
            ));
    }

    @Test
    @DisplayName("성공: 허브 링크 목록 조회 api 호출 시")
    void findHubLinks() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("linkBundleId", "1");
        params.add("page", "0");
        params.add("size", "20");
        long hubId = 1;
        FindLinkResponse findLink = new FindLinkResponse(1L, "url", "설명",
            LinkFixture.DEFAULT_EXPIRED_AT);
        FindLinksResponse response = new FindLinksResponse(List.of(findLink), 1, false);

        given(linkService.findHubLinks(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/hubs/{hubId}/links", hubId)
            .header(AUTHORIZATION, bearerAccessToken)
            .params(params));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("액세스 토큰").optional()
                ),
                pathParameters(
                    parameterWithName("hubId").description("허브 ID")
                ),
                queryParameters(
                    parameterWithName("linkBundleId").description("링크 묶음 ID"),
                    parameterWithName("page").description("페이지").optional(),
                    parameterWithName("size").description("사이즈").optional(),
                    parameterWithName("linkOrderBy").description("정렬 기준").optional()
                ),
                responseFields(
                    fieldWithPath("links").type(JsonFieldType.ARRAY).description("허브 링크 목록"),
                    fieldWithPath("links[].linkId").type(JsonFieldType.NUMBER).description("링크 ID"),
                    fieldWithPath("links[].url").type(JsonFieldType.STRING).description("링크 url"),
                    fieldWithPath("links[].description").type(JsonFieldType.STRING)
                        .description("링크 설명"),
                    fieldWithPath("links[].expiredAt").type(JsonFieldType.STRING).description("링크 만료일"),
                    fieldWithPath("totalElements").type(JsonFieldType.NUMBER)
                        .description("총 요소 개수"),
                    fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 여부")
                )
            ));
    }

    @Test
    @DisplayName("성공: 회원 링크 삭제 api 호출 시")
    void deleteLink() throws Exception {
        //given
        given(linkService.deleteLink(any())).willReturn(new DeleteLinkResponse(1L));

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/links/{linkId}", 1L)
            .header(AUTHORIZATION, bearerAccessToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("linkId").description("링크 ID")
                ),
                responseFields(
                    fieldWithPath("linkId").description("삭제된 링크 ID")
                )
            ));
    }

    @Test
    @DisplayName("성공: 허브 링크 삭제 api 호출 시")
    void deleteHubLink() throws Exception {
        //given
        given(linkService.deleteHubLink(any())).willReturn(new DeleteLinkResponse(1L));

        //when
        ResultActions resultActions = mockMvc.perform(
            delete("/api/hubs/{hubId}/links/{linkId}", 1L, 1L)
                .header(AUTHORIZATION, bearerAccessToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("hubId").description("허브 ID"),
                    parameterWithName("linkId").description("링크 ID")
                ),
                responseFields(
                    fieldWithPath("linkId").description("삭제된 링크 ID")
                )
            ));
    }
}