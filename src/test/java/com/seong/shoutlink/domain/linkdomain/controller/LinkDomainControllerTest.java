package com.seong.shoutlink.domain.linkdomain.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.seong.shoutlink.base.BaseControllerTest;
import com.seong.shoutlink.domain.linkdomain.service.response.FindLinkDomainDetailResponse;
import com.seong.shoutlink.domain.linkdomain.service.response.FindLinkDomainLinkResponse;
import com.seong.shoutlink.domain.linkdomain.service.response.FindLinkDomainLinksResponse;
import com.seong.shoutlink.domain.linkdomain.service.response.FindLinkDomainResponse;
import com.seong.shoutlink.domain.linkdomain.service.response.FindLinkDomainsResponse;
import com.seong.shoutlink.domain.linkdomain.service.response.FindRootDomainsResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class LinkDomainControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("루트 도메인 자동 완성 API 호출 시")
    void findRootDomains() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("keyword", "searchKeyword");
        params.add("size", "20");
        FindRootDomainsResponse response = new FindRootDomainsResponse(List.of("github.com"));

        given(linkDomainService.findRootDomains(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/domains/search")
            .params(params));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                queryParameters(
                    parameterWithName("keyword").description("검색어"),
                    parameterWithName("size").description("검색어 목록 사이즈")
                ),
                responseFields(
                    fieldWithPath("rootDomains").type(ARRAY).description("루트 도메인 목록")
                )
            ));
    }

    @Test
    @DisplayName("성공: 도메인 목록 조회 API 호출 시")
    void findDomains() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("keyword", "searchKeyword");
        params.add("page", "0");
        params.add("size", "10");
        List<FindLinkDomainResponse> domains = List.of(new FindLinkDomainResponse(1L, "github.com"));
        FindLinkDomainsResponse response = new FindLinkDomainsResponse(domains, 1, false);

        given(linkDomainService.findLinkDomains(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/domains")
            .params(params));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                queryParameters(
                    parameterWithName("keyword").description("검색어"),
                    parameterWithName("page").description("페이지"),
                    parameterWithName("size").description("사이즈")
                ),
                responseFields(
                    fieldWithPath("domains").type(ARRAY).description("도메인 목록"),
                    fieldWithPath("domains[].domainId").type(NUMBER).description("도메인 ID"),
                    fieldWithPath("domains[].rootDomain").type(STRING).description("루트 도메인"),
                    fieldWithPath("totalElements").type(NUMBER).description("총 요소 개수"),
                    fieldWithPath("hasNext").type(BOOLEAN).description("다음 페이지 여부")
                )
            ));
    }

    @Test
    @DisplayName("성공: 도메인 정보 단건 조회 api 호출 시")
    void findDomain() throws Exception {
        //given
        FindLinkDomainDetailResponse response = new FindLinkDomainDetailResponse(1L, "github.com");
        given(linkDomainService.findLinkDomain(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/domains/{domainId}", 1L));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("domainId").description("도메인 ID")
                ),
                responseFields(
                    fieldWithPath("domainId").type(NUMBER).description("도메인 ID"),
                    fieldWithPath("rootDomain").type(STRING).description("루트 도메인")
                )
            ));
    }

    @Test
    @DisplayName("성공: 도메인 링크 목록 조회 api 호출 시")
    void findDomainLinks() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("size", "10");

        FindLinkDomainLinkResponse content = new FindLinkDomainLinkResponse(1L, "github.com/hseong3243", 1);
        FindLinkDomainLinksResponse response = new FindLinkDomainLinksResponse(List.of(content), 1L, false);
        given(linkDomainService.findLinkDomainLinks(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/domains/{domainId}/links", 1L)
            .params(params));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("domainId").description("도메인 ID")
                ),
                queryParameters(
                    parameterWithName("page").description("페이지"),
                    parameterWithName("size").description("사이즈")
                ),
                responseFields(
                    fieldWithPath("links").type(ARRAY).description("링크 목록"),
                    fieldWithPath("links[].linkId").type(NUMBER).description("링크 ID"),
                    fieldWithPath("links[].url").type(STRING).description("링크 url"),
                    fieldWithPath("links[].aggregationCount").type(NUMBER).description("링크 집계 카운트"),
                    fieldWithPath("totalElements").type(NUMBER).description("총 요소 개수"),
                    fieldWithPath("hasNext").type(BOOLEAN).description("다음 페이지 여부")
                )
            ));
    }
}