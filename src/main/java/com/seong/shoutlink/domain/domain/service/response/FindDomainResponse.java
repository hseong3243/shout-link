package com.seong.shoutlink.domain.domain.service.response;

import com.seong.shoutlink.domain.domain.Domain;

public record FindDomainResponse(Long domainId, String rootDomain) {

    public static FindDomainResponse from(Domain domain) {
        return new FindDomainResponse(domain.getDomainId(), domain.getRootDomain());
    }
}
