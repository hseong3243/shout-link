package com.seong.shoutlink.fixture;

import com.seong.shoutlink.domain.auth.FakePasswordEncoder;
import com.seong.shoutlink.domain.auth.PasswordEncoder;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.MemberRole;

public final class MemberFixture {

    public static final long MEMBER_ID = 1L;
    public static final String MEMBER_EMAIL = "stub@stub.com";
    public static final String MEMBER_PASSWORD = "stub123!";
    public static final String MEMBER_NICKNAME = "stubMember";
    public static final MemberRole MEMBER_ROLE = MemberRole.ROLE_USER;

    public static Member member() {
        PasswordEncoder passwordEncoder = new FakePasswordEncoder();
        return new Member(MEMBER_ID, MEMBER_EMAIL, passwordEncoder.encode(MEMBER_PASSWORD),
            MEMBER_NICKNAME, MEMBER_ROLE);
    }
}
