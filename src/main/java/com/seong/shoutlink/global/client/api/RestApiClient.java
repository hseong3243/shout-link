package com.seong.shoutlink.global.client.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seong.shoutlink.domain.common.ApiClient;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class RestApiClient implements ApiClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public RestApiClient(ObjectMapper objectMapper) {
        restClient = RestClient.create();
        this.objectMapper = objectMapper;
    }

    @Override
    public Map<String, Object> post(
        String url,
        Map<String, List<String>> uriVariables,
        Map<String, List<String>> headers,
        String requestBody) {
        ResponseEntity<String> entity = restClient.post()
            .uri(url, uriVariables)
            .headers(httpHeaders -> httpHeaders.putAll(headers))
            .body(requestBody)
            .retrieve()
            .toEntity(String.class);

        try {
            return objectMapper.readValue(entity.getBody(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new ShoutLinkException("API 응답을 읽는데 실패하였습니다.", ErrorCode.ILLEGAL_ARGUMENT);
        }
    }
}
