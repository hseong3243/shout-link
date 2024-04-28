package com.seong.shoutlink.domain.link;

import lombok.Getter;

@Getter
public class LinkWithLinkBundle {

    private final Link link;
    private final LinkBundle linkBundle;

    public LinkWithLinkBundle(Link link, LinkBundle linkBundle) {
        this.link = link;
        this.linkBundle = linkBundle;
    }
}
