package com.seong.shoutlink.domain.linkdomain.service.response;

import com.seong.shoutlink.domain.linkdomain.service.result.LinkDomainLinkResult;
import java.util.List;

public record FindLinkDomainLinksResponse(
    List<FindLinkDomainLinkResponse> links,
    long totalElements,
    boolean hasNext) {


    public static FindLinkDomainLinksResponse of(
        List<LinkDomainLinkResult> links,
        long totalElements,
        boolean hasNext) {
        List<FindLinkDomainLinkResponse> content = links.stream()
            .map(link -> new FindLinkDomainLinkResponse(
                link.linkId(),
                link.url(),
                link.aggregationCount())
            ).toList();
        return new FindLinkDomainLinksResponse(content, totalElements, hasNext);
    }
}
