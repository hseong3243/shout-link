package com.seong.shoutlink.domain.link.link.service.response;

import com.seong.shoutlink.domain.link.link.Link;
import java.time.LocalDateTime;

public record FindLinkResponse(Long linkId, String url, String description, LocalDateTime expiredAt) {

    public static FindLinkResponse from(Link link) {
        return new FindLinkResponse(link.getLinkId(), link.getUrl(), link.getDescription(), link.getExpiredAt());
    }
}
