package com.seong.shoutlink.domain.link.linkdomain.service.response;

import com.seong.shoutlink.domain.link.linkdomain.LinkDomain;

public record FindLinkDomainResponse(Long domainId, String rootDomain) {

    public static FindLinkDomainResponse from(LinkDomain linkDomain) {
        return new FindLinkDomainResponse(linkDomain.getDomainId(), linkDomain.getRootDomain());
    }
}
