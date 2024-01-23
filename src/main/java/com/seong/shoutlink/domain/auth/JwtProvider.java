package com.seong.shoutlink.domain.auth;

import com.seong.shoutlink.domain.auth.service.response.ClaimsResponse;
import com.seong.shoutlink.domain.auth.service.response.TokenResponse;
import com.seong.shoutlink.domain.member.MemberRole;

public interface JwtProvider {
    TokenResponse createToken(Long memberId, MemberRole memberRole);

    ClaimsResponse parseAccessToken(String accessToken);
}
