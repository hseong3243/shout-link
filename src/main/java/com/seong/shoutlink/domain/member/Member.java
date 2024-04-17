package com.seong.shoutlink.domain.member;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.Getter;

@Getter
public class Member {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]{1,64}@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[a-zA-Z가-힣0-9]{1,20}$");

    private final Long memberId;
    private final String email;
    private final String password;
    private final String nickname;
    private final MemberRole memberRole;

    public Member(String email, String password, String nickname, MemberRole memberRole) {
        this(null, email, password, nickname, memberRole);
    }

    public Member(Long memberId, String email, String password, String nickname,
        MemberRole memberRole) {
        validateEmail(email);
        validatePassword(password);
        validateNickname(nickname);
        validateMemberRole(memberRole);
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.memberRole = memberRole;
    }

    private void validateEmail(String email) {
        if(Objects.isNull(email)) {
            throw new ShoutLinkException("이메일은 필수입니다.", ErrorCode.ILLEGAL_ARGUMENT);
        }
        if(!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ShoutLinkException("잘못된 이메일 형식입니다.", ErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    private void validatePassword(String password) {
        if(Objects.isNull(password)) {
            throw new ShoutLinkException("비밀번호는 필수입니다.", ErrorCode.ILLEGAL_ARGUMENT);
        }
        if(password.isBlank()) {
            throw new ShoutLinkException("비밀번호는 공백일 수 없습니다.", ErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    private void validateNickname(String nickname) {
        if(Objects.isNull(nickname)) {
            throw new ShoutLinkException("닉네임은 필수입니다.", ErrorCode.ILLEGAL_ARGUMENT);
        }
        if(!NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new ShoutLinkException("닉네임은 영문 대소문자, 한글, 숫자 1자 이상, 20자 이하여야 합니다.",
                ErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    private void validateMemberRole(MemberRole memberRole) {
        if(Objects.isNull(memberRole)) {
            throw new ShoutLinkException("회원 역할은 필수입니다.", ErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    public boolean isEqualToMemberId(Long memberId) {
        return Objects.equals(this.memberId, memberId);
    }
}
