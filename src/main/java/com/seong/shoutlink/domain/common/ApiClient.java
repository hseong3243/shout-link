package com.seong.shoutlink.domain.common;

import java.util.List;
import java.util.Map;

public interface ApiClient {

    Map<String, Object> post(
        String url,
        Map<String, List<String>> uriVariables,
        Map<String, List<String>> headers,
        String requestBody);
}
