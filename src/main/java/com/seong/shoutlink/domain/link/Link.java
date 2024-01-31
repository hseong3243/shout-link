package com.seong.shoutlink.domain.link;

import lombok.Getter;

@Getter
public class Link {

    private Long linkId;
    private String url;
    private String description;


    public Link(String url, String description) {
        this(null, url, description);
    }

    public Link(Long linkId, String url, String description) {
        this.linkId = linkId;
        this.url = url;
        this.description = description;
    }
}
