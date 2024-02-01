package com.seong.shoutlink.domain.linkbundle.service.response;

import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import java.util.List;

public record FindLinkBundlesResponse(List<FindLinkBundleResponse> linkBundles) {

    public static FindLinkBundlesResponse from(List<LinkBundle> linkBundles) {
        List<FindLinkBundleResponse> content = linkBundles.stream()
            .map(FindLinkBundleResponse::from)
            .toList();
        return new FindLinkBundlesResponse(content);
    }
}
