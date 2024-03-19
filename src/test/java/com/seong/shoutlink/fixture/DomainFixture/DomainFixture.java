package com.seong.shoutlink.fixture.DomainFixture;

import com.seong.shoutlink.domain.domain.Domain;

public class DomainFixture {

    public static final long DOMAIN_ID = 1L;
    public static final String ROOT_DOMAIN = "github.com";

    public static Domain domain() {
        return new Domain(DOMAIN_ID, ROOT_DOMAIN);
    }

    public static Domain domain(String url) {
        return new Domain(DOMAIN_ID, url);
    }
}
