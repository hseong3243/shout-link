package com.seong.shoutlink.domain.link;

import lombok.Getter;

@Getter
public class Link {

    private final Long linkId;
    private final String url;
    private final String description;


    public Link(String url, String description) {
        this(null, url, description);
    }

    public Link(Long linkId, String url, String description) {
        this.linkId = linkId;
        this.url = url;
        this.description = description;
    }
}
