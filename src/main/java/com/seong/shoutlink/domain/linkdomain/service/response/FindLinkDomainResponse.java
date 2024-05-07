package com.seong.shoutlink.domain.linkdomain.service.response;

import com.seong.shoutlink.domain.linkdomain.LinkDomain;

public record FindLinkDomainResponse(Long domainId, String rootDomain) {

    public static FindLinkDomainResponse from(LinkDomain linkDomain) {
        return new FindLinkDomainResponse(linkDomain.getDomainId(), linkDomain.getRootDomain());
    }
}
