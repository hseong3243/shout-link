package com.seong.shoutlink.domain.link.linkdomain.service.response;

import com.seong.shoutlink.domain.link.linkdomain.LinkDomain;
import java.util.List;

public record FindLinkDomainsResponse(
    List<FindLinkDomainResponse> domains,
    long totalElements,
    boolean hasNext) {

    public static FindLinkDomainsResponse of(List<LinkDomain> linkDomains, long totalElements,
        boolean hasNext) {
        List<FindLinkDomainResponse> content = linkDomains.stream()
            .map(FindLinkDomainResponse::from)
            .toList();
        return new FindLinkDomainsResponse(content, totalElements, hasNext);
    }
}
