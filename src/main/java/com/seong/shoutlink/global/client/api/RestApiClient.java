package com.seong.shoutlink.global.client.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seong.shoutlink.domain.common.ApiClient;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Slf4j
public class RestApiClient implements ApiClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public RestApiClient(ObjectMapper objectMapper) {
        restClient = RestClient.builder()
            .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                log.error("[API] API 서버 에러가 발생하였습니다. [response - status={}, body={}]",
                    response.getStatusCode(),
                    new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
                throw new ApiException("API 서버 에러가 발생하였습니다.");
            })
            .build();
        this.objectMapper = objectMapper;
    }

    @Override
    public Map<String, Object> post(
        String url,
        Map<String, List<String>> uriVariables,
        Map<String, List<String>> headers,
        String requestBody) {
        String responseBody = restClient.post()
            .uri(url, uriVariables)
            .headers(httpHeaders -> httpHeaders.putAll(headers))
            .body(requestBody)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                log.error("[API] 요청 형식이 잘못되었습니다. "
                        + "[request - url={}, uriVariables={}, headers={}, body={}] "
                        + "[response - body={}",
                    url, uriVariables, headers, requestBody,
                    new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
                throw new ApiException("요청 형식이 잘못되었습니다.");
            })
            .body(String.class);

        try {
            return objectMapper.readValue(responseBody, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("[API] API 응답을 읽는데 실패하였습니다. [response - body={}]", responseBody);
            throw new ApiException("API 응답을 읽는데 실패하였습니다.");
        }
    }
}
