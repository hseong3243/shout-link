package com.seong.shoutlink.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.domain.auth.FakePasswordEncoder;
import com.seong.shoutlink.domain.auth.service.request.CreateMemberCommand;
import com.seong.shoutlink.domain.auth.service.response.CreateMemberResponse;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.MemberRole;
import com.seong.shoutlink.domain.member.repository.StubMemberRepository;
import com.seong.shoutlink.global.exception.ErrorCode;
import com.seong.shoutlink.global.exception.ShoutLinkException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AuthServiceTest {

    FakePasswordEncoder passwordEncoder = new FakePasswordEncoder();

    @Nested
    @DisplayName("회원 생성 시")
    class CreateMemberTest {

        Member savedMember;
        StubMemberRepository memberRepository;
        AuthService authService;

        @BeforeEach
        void setUp() {
            String email = "stub@stub.com";
            String password = "stub123!";
            String nickname = "stub";
            MemberRole memberRole = MemberRole.USER;
            savedMember = new Member(email, password, nickname, memberRole);
            memberRepository
                = new StubMemberRepository(savedMember);
            authService = new AuthService(memberRepository, passwordEncoder);
        }

        @ParameterizedTest
        @CsvSource({"asdf123!", "asdfasdf12341234!@#$"})
        @DisplayName("성공: 비밀번호가 영어 소문자, 특수문자, 숫자를 모두 포함하여 8자 이상, 20자 이하이다.")
        void createMember(String validPassword) {
            //given
            CreateMemberCommand createMemberCommand
                = new CreateMemberCommand("email@email.com", validPassword, "닉네임");

            //when
            CreateMemberResponse response = authService.createMember(createMemberCommand);

            //then
            assertThat(response.memberId()).isNotNull();
        }

        @ParameterizedTest
        @CsvSource({"asdf", "asdf1234", "asdf!@#$", "1234!@#$", "a1!", "thisisovertwentycharacters!@#$123"})
        @DisplayName("예외(illegalArgument): 비밀번호가 잘못된 형식")
        void invalidPassword(String invalidPassword) {
            //given
            CreateMemberCommand createMemberCommand
                = new CreateMemberCommand("email@email.com", invalidPassword, "닉네임");

            //when
            Exception exception = catchException(
                () -> authService.createMember(createMemberCommand));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.ILLEGAL_ARGUMENT);
        }

        @Test
        @DisplayName("예외(duplicateEmail): 중복된 이메일")
        void duplicateEmail_WhenEmailIsDuplicate() {
            //given
            String duplicateEmail = savedMember.getEmail();
            CreateMemberCommand createMemberCommand
                = new CreateMemberCommand(duplicateEmail, "asdf123!", "닉네임");

            //when
            Exception exception = catchException(
                () -> authService.createMember(createMemberCommand));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.DUPLICATE_EMAIL);
        }

        @Test
        @DisplayName("예외(duplicateNickname): 중복된 닉네임")
        void duplicateNickname_WhenNicknameIsDuplicate() {
            //given
            String duplicateNickname = savedMember.getNickname();
            CreateMemberCommand createMemberCommand
                = new CreateMemberCommand("asdf@asdf.com", "asdf123!", duplicateNickname);

            //when
            Exception exception = catchException(
                () -> authService.createMember(createMemberCommand));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.DUPLICATE_NICKNAME);
        }
    }
}