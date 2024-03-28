package com.seong.shoutlink.domain.domain.service.result;

import java.util.List;

public record DomainLinkPaginationResult(
    List<DomainLinkResult> links,
    long totalElements,
    boolean hasNext) {


}
