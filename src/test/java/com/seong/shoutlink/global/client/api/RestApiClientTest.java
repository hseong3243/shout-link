package com.seong.shoutlink.global.client.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.base.BaseIntegrationTest;
import java.io.IOException;
import java.util.Map;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RestApiClientTest extends BaseIntegrationTest {

    @Autowired
    private RestApiClient restApiClient;

    MockWebServer mockServer;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @Nested
    @DisplayName("생성하면")
    class CreateTest {

        @Test
        @DisplayName("예외(apiException): 5xx 에러일 때")
        void whenStatusCode_5xx() {
            //given
            MockResponse mockResponse = new MockResponse()
                .setResponseCode(500);
            mockServer.enqueue(mockResponse);
            HttpUrl baseUrl = mockServer.url("/server-error");

            //when
            Exception exception = catchException(
                () -> restApiClient.post(baseUrl.toString(), Map.of(), Map.of(), ""));

            //then
            assertThat(exception).isInstanceOf(ApiException.class);
        }
    }

    @Nested
    @DisplayName("post 호출 시")
    class WhenPost {

        @Test
        @DisplayName("성공")
        void post() {
            //given
            MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody("{\"content\": \"hello\"}");
            mockServer.enqueue(mockResponse);
            HttpUrl baseUrl = mockServer.url("/success");

            //when
            Map<String, Object> response = restApiClient.post(baseUrl.toString(), Map.of(),
                Map.of(), "");

            //then
            assertThat(response.get("content")).isEqualTo("hello");
        }

        @Test
        @DisplayName("예외(apiException): 4xx 에러일 때")
        void whenStatusCode_4xx() {
            //given
            MockResponse mockResponse = new MockResponse()
                .setResponseCode(400);
            mockServer.enqueue(mockResponse);
            HttpUrl baseUrl = mockServer.url("/client-error");

            //when
            Exception exception = catchException(
                () -> restApiClient.post(baseUrl.toString(), Map.of(), Map.of(), ""));

            //then
            assertThat(exception).isInstanceOf(ApiException.class);
        }

        @Test
        @DisplayName("예외(apiException): 응답 형식이 json이 아닐 때")
        void whenResponse_isNotJson() {
            //given
            MockResponse mockResponse = new MockResponse()
                .setBody("not json");
            mockServer.enqueue(mockResponse);
            HttpUrl baseUrl = mockServer.url("/not-json");

            //when
            Exception exception = catchException(
                () -> restApiClient.post(baseUrl.toString(), Map.of(), Map.of(), ""));

            //then
            assertThat(exception).isInstanceOf(ApiException.class);
        }
    }
}
