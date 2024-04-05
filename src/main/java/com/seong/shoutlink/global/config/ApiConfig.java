package com.seong.shoutlink.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seong.shoutlink.domain.common.ApiClient;
import com.seong.shoutlink.global.client.api.RestApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Bean
    public ApiClient apiClient(ObjectMapper objectMapper) {
        return new RestApiClient(objectMapper);
    }
}
