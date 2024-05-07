package com.seong.shoutlink.domain.link.linkdomain.service.response;

import com.seong.shoutlink.domain.link.linkdomain.LinkDomain;

public record FindLinkDomainDetailResponse(Long domainId, String rootDomain) {

    public static FindLinkDomainDetailResponse from(LinkDomain linkDomain) {
        return new FindLinkDomainDetailResponse(linkDomain.getDomainId(), linkDomain.getRootDomain());
    }
}
