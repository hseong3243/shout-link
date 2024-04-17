package com.seong.shoutlink.domain.auth;

import com.seong.shoutlink.domain.member.service.PasswordEncoder;

public class FakePasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String rawPassword) {
        return new StringBuilder(rawPassword).reverse().toString();
    }

    @Override
    public boolean isMatches(String rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }

    @Override
    public boolean isNotMatches(String rawPassword, String encodedPassword) {
        return !isMatches(rawPassword, encodedPassword);
    }
}
