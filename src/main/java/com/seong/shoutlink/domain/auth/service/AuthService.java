package com.seong.shoutlink.domain.auth.service;

import com.seong.shoutlink.domain.auth.PasswordEncoder;
import com.seong.shoutlink.domain.auth.service.request.CreateMemberCommand;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.MemberRole;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import com.seong.shoutlink.global.exception.ErrorCode;
import com.seong.shoutlink.global.exception.ShoutLinkException;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Pattern PASSWORD_PATTEN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,20}$");

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private void validatePassword(CreateMemberCommand command) {
        if(!PASSWORD_PATTEN.matcher(command.password()).matches()) {
            throw new ShoutLinkException("비밀번호는 영어 소문자, 특수문자, 숫자 8자 이상, 20자 이하여야 합니다.",
                ErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    public Long createMember(CreateMemberCommand command) {
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
            MemberRole.USER);
        return memberRepository.save(member);
    }
}
