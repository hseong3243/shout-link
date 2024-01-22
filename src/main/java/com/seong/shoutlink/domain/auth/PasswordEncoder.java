package com.seong.shoutlink.domain.auth;

public interface PasswordEncoder {

    String encode(String rawPassword);
}
