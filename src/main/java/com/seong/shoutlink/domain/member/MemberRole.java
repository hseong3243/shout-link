package com.seong.shoutlink.domain.member;

import java.util.List;
import lombok.Getter;

@Getter
public enum MemberRole {
    USER(Constants.ROLE_USER, List.of(Constants.ROLE_USER)),
    ADMIN(Constants.ROLE_ADMIN, List.of(Constants.ROLE_USER, Constants.ROLE_ADMIN));

    private final String value;
    private final List<String> authorities;

    MemberRole(String value, List<String> authorities) {
        this.value = value;
        this.authorities = authorities;
    }

    private static class Constants {

        private static final String ROLE_USER = "ROLE_USER";
        private static final String ROLE_ADMIN = "ROLE_ADMIN";
    }
}
