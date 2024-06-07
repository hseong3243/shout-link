package com.seong.shoutlink.domain.link.link;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Link {

    private final Long linkId;
    private final String url;
    private final String description;
    private final LocalDateTime expiredAt;


    public Link(String url, String description, LocalDateTime expiredAt) {
        this(null, url, description, expiredAt);
    }

    public Link(Long linkId, String url, String description, LocalDateTime expiredAt) {
        this.linkId = linkId;
        this.url = url;
        this.description = description;
        this.expiredAt = expiredAt;
    }
}
