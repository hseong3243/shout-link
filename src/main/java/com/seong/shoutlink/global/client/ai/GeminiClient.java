package com.seong.shoutlink.global.client.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seong.shoutlink.domain.common.ApiClient;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.tag.service.AutoGenerativeClient;
import com.seong.shoutlink.domain.tag.service.ai.GenerateAutoTagCommand;
import com.seong.shoutlink.domain.tag.service.ai.GeneratedTag;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeminiClient implements AutoGenerativeClient {

    private static final Map<String, List<String>> HEADERS = new HashMap<>();
    private static final Map<String, List<String>> URI_VARIABLES = Map.of();
    private static final String API_KEY_HEADER = "x-goog-api-key";
    private static final String GENERATE_TAG_PROMPT =
        """
        주어진 링크 묶음 설명, 링크 설명, url을 통해 사용자의 관심사를 추론하세요.
        추론한 관심사는 {0}개의 키워드로 요약해서 응답하세요.
        각 키워드는 10자 이내여야 하며 둘 이상인 경우 ,로 구분하세요.
        """;

    static {
        HEADERS.put("Content-Type", List.of("application/json"));
    }

    private final String url;
    private final ObjectMapper objectMapper;
    private final ApiClient apiClient;

    public GeminiClient(String url, String apiKey, ObjectMapper objectMapper, ApiClient apiClient) {
        this.url = url;
        this.objectMapper = objectMapper;
        this.apiClient = apiClient;
        HEADERS.put(API_KEY_HEADER, List.of(apiKey));
    }

    @Override
    public List<GeneratedTag> generateTags(GenerateAutoTagCommand command) {
        String requestPrompt = MessageFormat.format(GENERATE_TAG_PROMPT, 1);
        AutoTagPrompt autoTagPrompt = new AutoTagPrompt(requestPrompt, command);
        GeminiRequest geminiRequest = GeminiRequest.create(autoTagPrompt.toPromptString());
        String requestBody = "";
        try {
            requestBody = objectMapper.writeValueAsString(geminiRequest);
        } catch (JsonProcessingException e) {
            throw new ShoutLinkException("문자열 변환에 실패하였습니다.", ErrorCode.ILLEGAL_ARGUMENT);
        }

        log.info("[Gemini] 태그 자동 생성 요청");
        Object rawCandidates = apiClient.post(url, URI_VARIABLES, HEADERS, requestBody)
            .get("candidates");
        List<GeminiResponse.Candidate> candidates
            = objectMapper.convertValue(rawCandidates, new TypeReference<>() {
        });

        log.info("[Gemini] 자동 생성된 태그 변환 시작");
        String[] splitTags = candidates.stream()
            .findFirst()
            .map(GeminiResponse.Candidate::getResponse)
            .map(tags -> tags.trim().split(","))
            .orElseThrow(() -> new ShoutLinkException("유효한 candidate가 포함되어 있지 않습니다.",
                ErrorCode.ILLEGAL_ARGUMENT));

        log.info("[Gemini] 자동 생성된 태그 변환 성공");
        return Arrays.stream(splitTags)
            .map(rawTag -> new GeneratedTag(rawTag.trim()))
            .toList();
    }
}
