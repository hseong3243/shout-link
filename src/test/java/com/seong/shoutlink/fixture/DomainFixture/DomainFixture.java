package com.seong.shoutlink.fixture.DomainFixture;

import com.seong.shoutlink.domain.linkdomain.LinkDomain;

public class DomainFixture {

    public static final long DOMAIN_ID = 1L;
    public static final String ROOT_DOMAIN = "github.com";

    public static LinkDomain domain() {
        return new LinkDomain(DOMAIN_ID, ROOT_DOMAIN);
    }

    public static LinkDomain domain(String url) {
        return new LinkDomain(DOMAIN_ID, url);
    }
}
