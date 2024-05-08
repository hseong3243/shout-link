package com.seong.shoutlink.domain.link.link.service.response;

import com.seong.shoutlink.domain.link.link.Link;
import java.util.List;

public record FindLinksResponse(List<FindLinkResponse> links,
                                long totalElements,
                                boolean hasNext) {

    public static FindLinksResponse of(List<Link> links, long totalElements, boolean hasNext) {
        List<FindLinkResponse> content = links.stream()
            .map(FindLinkResponse::from)
            .toList();
        return new FindLinksResponse(content, totalElements, hasNext);
    }
}
