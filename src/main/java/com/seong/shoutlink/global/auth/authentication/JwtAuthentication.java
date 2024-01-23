package com.seong.shoutlink.global.auth.authentication;

import com.seong.shoutlink.domain.member.MemberRole;
import java.util.List;

public record JwtAuthentication(
    Long memberId,
    MemberRole memberRole,
    String accessToken) implements Authentication {

    @Override
    public Long getPrincipal() {
        return memberId;
    }

    @Override
    public List<String> getAuthorities() {
        return memberRole.getAuthorities();
    }

    @Override
    public String getCredentials() {
        return accessToken;
    }
}
