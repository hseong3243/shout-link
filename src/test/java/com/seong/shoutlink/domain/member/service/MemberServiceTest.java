package com.seong.shoutlink.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.*;

import com.seong.shoutlink.domain.auth.FakePasswordEncoder;
import com.seong.shoutlink.domain.common.StubEventPublisher;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.repository.StubMemberRepository;
import com.seong.shoutlink.domain.member.service.request.CreateMemberCommand;
import com.seong.shoutlink.domain.member.service.response.CreateMemberResponse;
import com.seong.shoutlink.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MemberServiceTest {

    MemberService memberService;
    StubMemberRepository memberRepository;
    FakePasswordEncoder passwordEncoder;
    StubEventPublisher eventPublisher;

    @Nested
    @DisplayName("createMember 호출 시")
    class CreateMemberTest {

        @BeforeEach
        void setUp() {
            memberRepository = new StubMemberRepository();
            passwordEncoder = new FakePasswordEncoder();
            eventPublisher = new StubEventPublisher();
            memberService = new MemberService(memberRepository, passwordEncoder, eventPublisher);
        }

        @ParameterizedTest
        @CsvSource({"asdf123!", "asdfasdf12341234!@#$"})
        @DisplayName("성공: 비밀번호가 영어 소문자, 특수문자, 숫자를 모두 포함하여 8자 이상, 20자 이하이다.")
        void createMember(String validPassword) {
            //given
            CreateMemberCommand createMemberCommand
                = new CreateMemberCommand("email@email.com", validPassword, "닉네임");

            //when
            CreateMemberResponse response = memberService.createMember(createMemberCommand);

            //then
            assertThat(response.memberId()).isNotNull();
        }

        @ParameterizedTest
        @CsvSource({"asdf", "asdf1234", "asdf!@#$", "1234!@#$", "a1!",
            "thisisovertwentycharacters!@#$123"})
        @DisplayName("예외(illegalArgument): 비밀번호가 잘못된 형식")
        void invalidPassword(String invalidPassword) {
            //given
            CreateMemberCommand createMemberCommand
                = new CreateMemberCommand("email@email.com", invalidPassword, "닉네임");

            //when
            Exception exception = catchException(
                () -> memberService.createMember(createMemberCommand));

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
            Member member = MemberFixture.member();
            memberRepository.stub(member);
            String duplicateEmail = member.getEmail();
            CreateMemberCommand createMemberCommand
                = new CreateMemberCommand(duplicateEmail, "asdf123!", "닉네임");

            //when
            Exception exception = catchException(
                () -> memberService.createMember(createMemberCommand));

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
            Member member = MemberFixture.member();
            memberRepository.stub(member);
            String duplicateNickname = member.getNickname();
            CreateMemberCommand createMemberCommand
                = new CreateMemberCommand("asdf@asdf.com", "asdf123!", duplicateNickname);

            //when
            Exception exception = catchException(
                () -> memberService.createMember(createMemberCommand));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.DUPLICATE_NICKNAME);
        }

        @Test
        @DisplayName("성공: 회원 생성 이벤트 발행함")
        void publishCreateMemberEvent() {
            //given
            CreateMemberCommand command
                = new CreateMemberCommand("email@email.com", "asdf123!", "nickname");

            //when
            CreateMemberResponse response = memberService.createMember(command);

            //then
            assertThat(eventPublisher.getPublishEventCount()).isEqualTo(1);
        }
    }
}
