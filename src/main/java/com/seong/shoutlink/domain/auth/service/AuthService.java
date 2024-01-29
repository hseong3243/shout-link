package com.seong.shoutlink.domain.auth.service;

import com.seong.shoutlink.domain.auth.JwtProvider;
import com.seong.shoutlink.domain.auth.PasswordEncoder;
import com.seong.shoutlink.domain.auth.service.event.CreateMemberEvent;
import com.seong.shoutlink.domain.auth.service.request.CreateMemberCommand;
import com.seong.shoutlink.domain.auth.service.request.LoginCommand;
import com.seong.shoutlink.domain.auth.service.response.CreateMemberResponse;
import com.seong.shoutlink.domain.auth.service.response.LoginResponse;
import com.seong.shoutlink.domain.auth.service.response.TokenResponse;
import com.seong.shoutlink.domain.common.EventPublisher;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.MemberRole;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Pattern PASSWORD_PATTEN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,20}$");

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final EventPublisher eventPublisher;

    private void validatePassword(CreateMemberCommand command) {
        if(!PASSWORD_PATTEN.matcher(command.password()).matches()) {
            throw new ShoutLinkException("비밀번호는 영어 소문자, 특수문자, 숫자 8자 이상, 20자 이하여야 합니다.",
                ErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    @Transactional
    public CreateMemberResponse createMember(CreateMemberCommand command) {
        validatePassword(command);
        memberRepository.findByEmail(command.email())
                .ifPresent(member -> {
                    throw new ShoutLinkException("중복된 이메일입니다.", ErrorCode.DUPLICATE_EMAIL);
                });
        memberRepository.findByNickname(command.nickname())
            .ifPresent(member -> {
                throw new ShoutLinkException("중복된 닉네임입니다.", ErrorCode.DUPLICATE_NICKNAME);
            });
        Member member = new Member(
            command.email(),
            passwordEncoder.encode(command.password()),
            command.nickname(),
            MemberRole.ROLE_USER);
        Long memberId = memberRepository.save(member);
        eventPublisher.publishEvent(new CreateMemberEvent(memberId));
        return new CreateMemberResponse(memberId);
    }

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
