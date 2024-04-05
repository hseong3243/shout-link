package com.seong.shoutlink.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seong.shoutlink.domain.tag.service.AutoGenerativeClient;
import com.seong.shoutlink.domain.common.ApiClient;
import com.seong.shoutlink.global.client.ai.GeminiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public AutoGenerativeClient autoGenerativeClient(
        @Value("${gemini.url}") String url,
        @Value("${gemini.api-key}") String apiKey,
        ObjectMapper objectMapper,
        ApiClient apiClient) {
        return new GeminiClient(url, apiKey, objectMapper, apiClient);
    }
}
