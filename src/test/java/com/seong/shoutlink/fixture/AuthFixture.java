package com.seong.shoutlink.fixture;

import com.seong.shoutlink.domain.auth.JwtProvider;
import com.seong.shoutlink.global.auth.jwt.JJwtProvider;

public final class AuthFixture {

    public static JwtProvider jwtProvider() {
        String issuer = "test";
        int expirySeconds = 3600;
        int refreshExpirySeconds = 18000;
        String secret = "thisisjusttestaccesssecretsodontworry";
        String refreshSecret = "thisisjusttestrefreshsecretsodontworry";
        return new JJwtProvider(issuer, expirySeconds, refreshExpirySeconds, secret, refreshSecret);
    }
}
