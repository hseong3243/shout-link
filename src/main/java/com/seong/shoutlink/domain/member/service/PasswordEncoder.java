package com.seong.shoutlink.domain.member.service;

public interface PasswordEncoder {

    String encode(String rawPassword);

    boolean isMatches(String rawPassword, String encodedPassword);

    boolean isNotMatches(String rawPassword, String encodedPassword);
}
