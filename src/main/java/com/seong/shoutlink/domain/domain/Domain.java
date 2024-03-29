package com.seong.shoutlink.domain.domain;

import lombok.Getter;

@Getter
public class Domain {

    private Long domainId;
    private String rootDomain;

    public Domain(String rootDomain) {
        this(null, rootDomain);
    }

    public Domain(Long domainId, String rootDomain) {
        this.domainId = domainId;
        this.rootDomain = rootDomain;
    }
}
