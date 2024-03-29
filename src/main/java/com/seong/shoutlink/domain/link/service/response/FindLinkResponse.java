package com.seong.shoutlink.domain.link.service.response;

import com.seong.shoutlink.domain.link.Link;

public record FindLinkResponse(Long linkId, String url, String description) {

    public static FindLinkResponse from(Link link) {
        return new FindLinkResponse(link.getLinkId(), link.getUrl(), link.getDescription());
    }
}
