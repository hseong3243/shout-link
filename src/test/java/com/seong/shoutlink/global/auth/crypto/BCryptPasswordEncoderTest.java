package com.seong.shoutlink.global.auth.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BCryptPasswordEncoderTest {

    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Nested
    @DisplayName("encode 호출 시")
    class EncodeTest {

        @Test
        @DisplayName("성공: 일방향 해싱됨")
        void encode() {
            //given
            String password = "asdf1234!";

            //when
            String encoded = bCryptPasswordEncoder.encode(password);

            //then
            assertThat(encoded).isNotEqualTo(password);
        }
    }

    @Nested
    @DisplayName("matches 호출 시")
    class MatchesTest {

        @Test
        @DisplayName("성공: 플레인 텍스트와 해시값이 동일하면 true")
        void matches_ThenTrue() {
            //given
            String password = "asdf1234!";
            String encoded = bCryptPasswordEncoder.encode(password);

            //when
            boolean result = bCryptPasswordEncoder.isMatches(password, encoded);

            //then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("성공: 플레인 텍스트와 해시값이 동일하지 않으면 false")
        void noneMatches_ThenFalse() {
            //given
            String password = "asdf1234!";
            String encoded = bCryptPasswordEncoder.encode(password);
            String noneMatches = password + "a";

            //when
            boolean result = bCryptPasswordEncoder.isMatches(noneMatches, encoded);

            //then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("noneMatches 호출 시")
    class NoneMatchesTest {

        @Test
        @DisplayName("성공: 플레인 텍스트와 해시값이 동일하지 않으면 true")
        void noneMatches_ThenTrue() {
            //given
            String password = "asdf1234!";
            String encoded = bCryptPasswordEncoder.encode(password);
            String noneMatch = password + "a";

            //when
            boolean result = bCryptPasswordEncoder.isNotMatches(noneMatch, encoded);

            //then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("성공: 플레인 텍스트와 해시값이 동일하면 false")
        void matches_ThenFalse() {
            //given
            String matches = "asdf1234!";
            String encoded = bCryptPasswordEncoder.encode(matches);

            //when
            boolean result = bCryptPasswordEncoder.isNotMatches(matches, encoded);

            //then
            assertThat(result).isFalse();
        }
    }
}
