package com.seong.shoutlink.domain.domain.service.response;

import com.seong.shoutlink.domain.domain.Domain;
import java.util.List;

public record FindDomainsResponse(
    List<FindDomainResponse> domains,
    long totalElements,
    boolean hasNext) {

    public static FindDomainsResponse of(List<Domain> domains, long totalElements,
        boolean hasNext) {
        List<FindDomainResponse> content = domains.stream()
            .map(FindDomainResponse::from)
            .toList();
        return new FindDomainsResponse(content, totalElements, hasNext);
    }
}
