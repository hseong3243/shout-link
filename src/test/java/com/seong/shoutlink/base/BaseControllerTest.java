package com.seong.shoutlink.base;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seong.shoutlink.base.BaseControllerTest.BaseControllerConfig;
import com.seong.shoutlink.domain.auth.JwtProvider;
import com.seong.shoutlink.domain.auth.service.AuthService;
import com.seong.shoutlink.fixture.AuthFixture;
import com.seong.shoutlink.global.auth.authentication.AuthenticationContext;
import com.seong.shoutlink.global.auth.authentication.JwtAuthenticationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest
@Import({RestDocsConfig.class, BaseControllerConfig.class})
@ExtendWith(RestDocumentationExtension.class)
public class BaseControllerTest {

    @TestConfiguration
    static class BaseControllerConfig {

        @Bean
        public JwtProvider jwtProvider() {
            return AuthFixture.jwtProvider();
        }

        @Bean
        public JwtAuthenticationProvider jwtAuthenticationProvider(JwtProvider jwtProvider) {
            return new JwtAuthenticationProvider(jwtProvider);
        }

        @Bean
        public AuthenticationContext authenticationContext() {
            return new AuthenticationContext();
        }
    }

    protected MockMvc mockMvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AuthService authService;

    @BeforeEach
    void setUp(
        WebApplicationContext applicationContext,
        RestDocumentationContextProvider documentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
            .alwaysDo(print())
            .alwaysDo(restDocs)
            .apply(
                MockMvcRestDocumentation.documentationConfiguration(documentationContextProvider))
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .build();
    }
}
