package com.seong.shoutlink.global.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seong.shoutlink.domain.link.link.Link;
import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.tag.service.ai.GenerateAutoTagCommand;
import com.seong.shoutlink.domain.link.link.LinkBundleAndLinks;
import com.seong.shoutlink.domain.tag.service.ai.GeneratedTag;
import com.seong.shoutlink.fixture.ApiFixture;
import com.seong.shoutlink.fixture.LinkBundleFixture;
import com.seong.shoutlink.fixture.LinkFixture;
import com.seong.shoutlink.global.client.ai.GeminiClient;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class GeminiClientTest {

    GeminiClient geminiClient;
    ObjectMapper objectMapper;
    StubApiClient apiClient;

    @BeforeEach
    void setUp() {
        apiClient = new StubApiClient();
        objectMapper = new ObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        geminiClient = new GeminiClient("url", "apiKey", objectMapper, apiClient);
    }

    @Nested
    @DisplayName("generateTags 호출 시")
    class GenerateTagsTest {

        @Test
        @DisplayName("성공")
        void generateTags() {
            //given
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            Link link = LinkFixture.link();
            LinkBundleAndLinks linkBundleAndLinks = new LinkBundleAndLinks(linkBundle,
                List.of(link));
            GenerateAutoTagCommand command = GenerateAutoTagCommand.create(
                List.of(linkBundleAndLinks), 3);

            apiClient.stub(ApiFixture.geminiResponse());

            //when
            List<GeneratedTag> generatedTags = geminiClient.generateTags(command);

            //then
            assertThat(generatedTags)
                .map(GeneratedTag::name)
                .containsExactly("태그1", "태그2", "태그3");
        }
    }
}