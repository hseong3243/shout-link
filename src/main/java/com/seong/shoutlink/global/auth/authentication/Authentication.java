package com.seong.shoutlink.global.auth.authentication;

import java.util.List;

public interface Authentication {

    Long getPrincipal();

    List<String> getAuthorities();

    String getCredentials();
}
