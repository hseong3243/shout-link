package com.seong.shoutlink.domain.domain.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DomainExtractorTest {

    @Nested
    @DisplayName("extractDomain 메서드 호출 시")
    class ExtractRootDomainTest {

        @ParameterizedTest
        @CsvSource(value = {
            "https://github.com/hseong3243/shout-link, github.com",
            "https://programmers.co.kr/, programmers.co.kr",
            "www.google.com/search?q=skip-protocol, google.com",
            "www.google.com/search?q=한글, google.com",
            "github.com, github.com"
        })
        @DisplayName("성공: 루트 도메인 추출된다.")
        void extractRootDomain(String url, String extractedDomain) {
            //given
            //when
            String domain = DomainExtractor.extractRootDomain(url);

            //then
            assertThat(domain).isEqualTo(extractedDomain);
        }

        @Test
        @DisplayName("예외(illegalArgument): url 형식이 아닐 때")
        void ignore_WhenInputValueIsNotUrl() {
            //given
            String url = "asdf";

            //when
            Exception exception = catchException(() -> DomainExtractor.extractRootDomain(url));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.ILLEGAL_ARGUMENT);
        }
    }
}
