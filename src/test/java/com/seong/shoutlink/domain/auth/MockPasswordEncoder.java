package com.seong.shoutlink.domain.auth;

public class MockPasswordEncoder implements PasswordEncoder{

    @Override
    public String encode(String rawPassword) {
        return new StringBuilder(rawPassword).reverse().toString();
    }
}
