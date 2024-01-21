package com.seong.shoutlink.domain.auth;

import org.springframework.stereotype.Service;

@Service
public class SimplePasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String rawPassword) {
        return new StringBuilder(rawPassword).reverse().toString();
    }
}
