package com.seong.shoutlink.global.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seong.shoutlink.domain.common.ApiClient;
import java.util.List;
import java.util.Map;

public class StubApiClient implements ApiClient {

    private String stubValue;

    public void stub(String jsonString) {
        stubValue = jsonString;
    }

    @Override
    public Map<String, Object> post(String url, Map<String, List<String>> uriVariables,
        Map<String, List<String>> headers, String requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(stubValue, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
