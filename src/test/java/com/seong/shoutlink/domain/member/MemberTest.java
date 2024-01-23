package com.seong.shoutlink.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.global.exception.ErrorCode;
import com.seong.shoutlink.global.exception.ShoutLinkException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MemberTest {

    @Nested
    @DisplayName("생성자 호출 시")
    class NewMember {

        private String email = "email@email.com";
        private String password = "1234";
        private String nickname = "nickname";
        private MemberRole memberRole = MemberRole.ROLE_USER;

        @Test
        @DisplayName("회원이 생성된다.")
        void newMemberTest() {
            //given
            //when
            Member member = new Member(email, password, nickname, memberRole);

            //then
            assertThat(member.getEmail()).isEqualTo(email);
            assertThat(member.getPassword()).isEqualTo(password);
            assertThat(member.getNickname()).isEqualTo(nickname);
        }

        @Test
        @DisplayName("예외(illegalArgument): 이메일이 null")
        void illegalArgumentEx_WhenEmailIsNull() {
            //given
            String nullEmail = null;

            //when
            Exception exception = catchException(
                () -> new Member(nullEmail, password, nickname, memberRole));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException)e).getErrorCode())
                .isEqualTo(ErrorCode.ILLEGAL_ARGUMENT);
        }

        @ParameterizedTest
        @CsvSource({"email", "email@asdf", "@asdf.com"})
        @DisplayName("예외(illegalArgument): 이메일이 형식을 만족하지 않음")
        void illegalArgument_WhenEmailIsInvalid(String invalidEmail) {
            //given
            //when
            Exception exception = catchException(
                () -> new Member(invalidEmail, password, nickname, memberRole));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException)e).getErrorCode())
                .isEqualTo(ErrorCode.ILLEGAL_ARGUMENT);
        }

        @Test
        @DisplayName("예외(illegalArgument): 비밀번호가 null")
        void illegalArgument_WhenPasswordIsNull() {
            //given
            String nullPassword = null;

            //when
            Exception exception = catchException(
                () -> new Member(email, nullPassword, nickname, memberRole));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException)e).getErrorCode())
                .isEqualTo(ErrorCode.ILLEGAL_ARGUMENT);
        }

        @Test
        @DisplayName("예외(illegalArgument): 비밀번호가 공백")
        void illegalArgument_WhenPasswordIsBlank() {
            //given
            String blankPassword = "";

            //when
            Exception exception = catchException(
                () -> new Member(email, blankPassword, nickname, memberRole));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException)e).getErrorCode())
                .isEqualTo(ErrorCode.ILLEGAL_ARGUMENT);
        }

        @Test
        @DisplayName("예외(illegalArgument): 닉네임이 null")
        void illegalArgument_WhenNicknameIsNull() {
            //given
            String nullNickname = null;

            //when
            Exception exception = catchException(
                () -> new Member(email, password, nullNickname, memberRole));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException)e).getErrorCode())
                .isEqualTo(ErrorCode.ILLEGAL_ARGUMENT);
        }

        @ParameterizedTest
        @CsvSource({"asdf##", "thisNicknameOver21ooo"})
        @DisplayName("예외(illegalArgument): 닉네임이 영문 대소문자, 한글, 숫자 1자 이상, 20자 이하가 아님")
        void illegalArgument_WhenNicknameIsInvalid(String invalidNickname) {
            //given
            //when
            Exception exception = catchException(
                () -> new Member(email, password, invalidNickname, memberRole));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException)e).getErrorCode())
                .isEqualTo(ErrorCode.ILLEGAL_ARGUMENT);
        }

        @Test
        @DisplayName("예외(illegalArgument): 회원 역할이 null")
        void illegalArgument_WhenMemberRoleIsNull() {
            //given
            MemberRole nullMemberRole = null;

            //when
            Exception exception = catchException(
                () -> new Member(email, password, nickname, nullMemberRole));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException)e).getErrorCode())
                .isEqualTo(ErrorCode.ILLEGAL_ARGUMENT);
        }
    }
}