package com.seong.shoutlink.domain.link.service.response;

import com.seong.shoutlink.domain.link.LinkBundle;

public record FindLinkBundleResponse(
    Long linkBundleId,
    String description,
    boolean isDefault) {

    public static FindLinkBundleResponse from(LinkBundle linkBundle) {
        return new FindLinkBundleResponse(
            linkBundle.getLinkBundleId(),
            linkBundle.getDescription(),
            linkBundle.isDefault());
    }
}
