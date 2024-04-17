package com.seong.shoutlink.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.domain.auth.FakePasswordEncoder;
import com.seong.shoutlink.domain.auth.JwtProvider;
import com.seong.shoutlink.domain.auth.service.request.LoginCommand;
import com.seong.shoutlink.domain.auth.service.response.LoginResponse;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.MemberRole;
import com.seong.shoutlink.domain.member.repository.StubMemberRepository;
import com.seong.shoutlink.global.auth.jwt.JJwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AuthServiceTest {

    FakePasswordEncoder passwordEncoder = new FakePasswordEncoder();
    JwtProvider jwtProvider = new JJwtProvider(
        "test", 3600, 18000,
        "thisisjusttestaccesssecretsodontworry",
        "thisisjusttestrefreshsecretsodontworry");

    @Nested
    @DisplayName("로그인 메서드 호출 시")
    class LoginTest {

        String savedMemberPassword;
        Member savedMember;
        StubMemberRepository memberRepository;
        AuthService authService;

        @BeforeEach
        void setUp() {
            String email = "stub@stub.com";
            savedMemberPassword = "stub123!";
            String password = passwordEncoder.encode(savedMemberPassword);
            String nickname = "stub";
            MemberRole memberRole = MemberRole.ROLE_USER;
            savedMember = new Member(1L, email, password, nickname, memberRole);
            memberRepository = new StubMemberRepository(savedMember);
            authService = new AuthService(
                memberRepository,
                passwordEncoder,
                jwtProvider);
        }

        @Test
        @DisplayName("성공: 액세스 토큰, 리프레시 토큰 반환")
        void login() {
            //given
            LoginCommand loginCommand = new LoginCommand(
                savedMember.getEmail(),
                savedMemberPassword);

            //when
            LoginResponse response = authService.login(loginCommand);

            //then
            assertThat(response.accessToken()).isNotBlank();
            assertThat(response.refreshToken()).isNotBlank();
        }

        @Test
        @DisplayName("예외(unauthenticated): 이메일이 일치하지 않음")
        void unauthenticated_WhenEmailIsNotMatches() {
            //given
            LoginCommand loginCommand = new LoginCommand(
                "a" + savedMember.getEmail(),
                savedMemberPassword);

            //when
            Exception exception = catchException(() -> authService.login(loginCommand));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.UNAUTHENTICATED);
        }

        @Test
        @DisplayName("예외(unauthenticated): 비밀번호가 일치하지 않음")
        void unauthenticated_WhenPasswordIsNotMatches() {
            //given
            LoginCommand loginCommand = new LoginCommand(
                savedMember.getEmail(),
                savedMemberPassword + "a"
            );

            //when
            Exception exception = catchException(() -> authService.login(loginCommand));

            //then
            assertThat(exception)
                .isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.UNAUTHENTICATED);
        }
    }
}