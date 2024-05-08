package com.seong.shoutlink.domain.link.linkdomain.service.result;

import com.seong.shoutlink.domain.link.linkdomain.LinkDomain;
import java.util.List;

public record LinkDomainPaginationResult(List<LinkDomain> linkDomains, long totalElements, boolean hasNext) {

}
