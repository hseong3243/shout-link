package com.seong.shoutlink.domain.linkdomain.service.result;

import java.util.List;

public record LinkDomainLinkPaginationResult(
    List<LinkDomainLinkResult> links,
    long totalElements,
    boolean hasNext) {


}
