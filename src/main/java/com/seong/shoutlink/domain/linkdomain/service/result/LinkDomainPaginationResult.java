package com.seong.shoutlink.domain.linkdomain.service.result;

import com.seong.shoutlink.domain.linkdomain.LinkDomain;
import java.util.List;

public record LinkDomainPaginationResult(List<LinkDomain> linkDomains, long totalElements, boolean hasNext) {

}
