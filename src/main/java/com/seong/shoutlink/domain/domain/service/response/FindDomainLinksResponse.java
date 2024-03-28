package com.seong.shoutlink.domain.domain.service.response;

import com.seong.shoutlink.domain.domain.service.result.DomainLinkResult;
import java.util.List;

public record FindDomainLinksResponse(
    List<FindDomainLinkResponse> links,
    long totalElements,
    boolean hasNext) {


    public static FindDomainLinksResponse of(
        List<DomainLinkResult> links,
        long totalElements,
        boolean hasNext) {
        List<FindDomainLinkResponse> content = links.stream()
            .map(link -> new FindDomainLinkResponse(
                link.linkId(),
                link.url(),
                link.aggregationCount())
            ).toList();
        return new FindDomainLinksResponse(content, totalElements, hasNext);
    }
}
