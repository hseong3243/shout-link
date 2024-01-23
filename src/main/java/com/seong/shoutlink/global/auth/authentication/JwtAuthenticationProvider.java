package com.seong.shoutlink.global.auth.authentication;

import com.seong.shoutlink.domain.auth.JwtProvider;
import com.seong.shoutlink.domain.auth.service.response.ClaimsResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationProvider {

    private final JwtProvider jwtProvider;

    public JwtAuthentication authenticate(String accessToken) {
        ClaimsResponse claims = jwtProvider.parseAccessToken(accessToken);
        return new JwtAuthentication(claims.memberId(), claims.memberRole(), accessToken);
    }
}
