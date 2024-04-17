package com.seong.shoutlink.domain.auth.service;

import com.seong.shoutlink.domain.auth.JwtProvider;
import com.seong.shoutlink.domain.auth.PasswordEncoder;
import com.seong.shoutlink.domain.auth.service.request.LoginCommand;
import com.seong.shoutlink.domain.auth.service.response.LoginResponse;
import com.seong.shoutlink.domain.auth.service.response.TokenResponse;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public LoginResponse login(LoginCommand command) {
        Member member = memberRepository.findByEmail(command.email())
            .orElseThrow(() -> new ShoutLinkException(
                "이메일/비밀번호가 일치하지 않습니다.", ErrorCode.UNAUTHENTICATED));
        if(passwordEncoder.isNotMatches(command.password(), member.getPassword())) {
            throw new ShoutLinkException("이메일/비밀번호가 일치하지 않습니다.", ErrorCode.UNAUTHENTICATED);
        }
        TokenResponse tokenResponse = jwtProvider.createToken(
            member.getMemberId(),
            member.getMemberRole());
        return new LoginResponse(
            member.getMemberId(),
            tokenResponse.accessToken(),
            tokenResponse.refreshToken());
    }
}
