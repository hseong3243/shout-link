package com.seong.shoutlink.domain.link.link;

import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;
import lombok.Getter;

@Getter
public class LinkBundleAndLink {

    private final Link link;
    private final LinkBundle linkBundle;

    public LinkBundleAndLink(Link link, LinkBundle linkBundle) {
        this.link = link;
        this.linkBundle = linkBundle;
    }
}
