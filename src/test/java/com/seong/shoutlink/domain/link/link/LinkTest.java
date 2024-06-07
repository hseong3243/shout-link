package com.seong.shoutlink.domain.link.link;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LinkTest {

    @Nested
    @DisplayName("링크 생성 시")
    class CreateLink {

        String url;
        String description;

        @BeforeEach
        void setUp() {
            url = "github.ocm";
            description = "깃허브";
        }

        @Test
        @DisplayName("성공: 만료일이 null이면 기본값은 9999년 12월 31일 23시 59분이다.")
        void createLink_WhenExpiredAtIsNull_ThenDefaultExpiredAt_9999_12_31_23_59() {
            //given
            LocalDateTime expiredAt = null;

            //when
            Link link = new Link(url, description, expiredAt);

            //then
            assertThat(link.getExpiredAt()).isEqualToIgnoringSeconds(
                LocalDateTime.of(9999, 12, 31, 23, 59));
        }

        @Test
        @DisplayName("성공: 만료일이 null이 아니면 입력값을 따른다.")
        void createLink_WhenExpiredAtIsNotNull_ThenFollowInput() {
            //given
            LocalDateTime expiredAt = LocalDateTime.of(2024, 12, 31, 23, 59);

            //when
            Link link = new Link(url, description, expiredAt);

            //when
            assertThat(link.getExpiredAt()).isEqualToIgnoringSeconds(expiredAt);
        }
    }
}
