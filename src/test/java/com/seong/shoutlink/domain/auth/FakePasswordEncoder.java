package com.seong.shoutlink.domain.auth;

public class FakePasswordEncoder implements PasswordEncoder{

    @Override
    public String encode(String rawPassword) {
        return new StringBuilder(rawPassword).reverse().toString();
    }
}
