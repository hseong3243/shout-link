package com.seong.shoutlink.domain.domain.service.response;

import com.seong.shoutlink.domain.domain.Domain;

public record FindDomainDetailResponse(Long domainId, String rootDomain) {

    public static FindDomainDetailResponse from(Domain domain) {
        return new FindDomainDetailResponse(domain.getDomainId(), domain.getRootDomain());
    }
}
