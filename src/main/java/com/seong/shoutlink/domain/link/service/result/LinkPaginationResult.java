package com.seong.shoutlink.domain.link.service.result;

import com.seong.shoutlink.domain.link.Link;
import java.util.List;

public record LinkPaginationResult(List<Link> links, long totalElements, boolean hasNext) {

}
