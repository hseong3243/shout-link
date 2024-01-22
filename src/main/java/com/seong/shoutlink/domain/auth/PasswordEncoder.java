package com.seong.shoutlink.domain.auth;

public interface PasswordEncoder {

    String encode(String rawPassword);

    boolean isMatches(String rawPassword, String encodedPassword);

    boolean isNotMatches(String rawPassword, String encodedPassword);
}
