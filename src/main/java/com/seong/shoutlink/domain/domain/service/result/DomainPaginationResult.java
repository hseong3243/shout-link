package com.seong.shoutlink.domain.domain.service.result;

import com.seong.shoutlink.domain.domain.Domain;
import java.util.List;

public record DomainPaginationResult(List<Domain> domains, long totalElements, boolean hasNext) {

}
