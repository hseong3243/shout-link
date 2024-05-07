package com.seong.shoutlink.domain.link.linkdomain;

import lombok.Getter;

@Getter
public class LinkDomain {

    private final Long domainId;
    private final String rootDomain;

    public LinkDomain(String rootDomain) {
        this(null, rootDomain);
    }

    public LinkDomain(Long domainId, String rootDomain) {
        this.domainId = domainId;
        this.rootDomain = rootDomain;
    }
}
