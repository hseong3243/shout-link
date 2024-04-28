package com.seong.shoutlink.domain.link.service.response;

import com.seong.shoutlink.domain.link.LinkBundle;
import java.util.List;

public record FindLinkBundlesResponse(List<FindLinkBundleResponse> linkBundles) {

    public static FindLinkBundlesResponse from(List<LinkBundle> linkBundles) {
        List<FindLinkBundleResponse> content = linkBundles.stream()
            .map(FindLinkBundleResponse::from)
            .toList();
        return new FindLinkBundlesResponse(content);
    }
}
