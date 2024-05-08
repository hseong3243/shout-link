package com.seong.shoutlink.domain.link.link;

import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;
import java.util.List;
import lombok.Getter;

@Getter
public class LinkBundleAndLinks {

    private final LinkBundle linkBundle;
    private final List<Link> links;

    public LinkBundleAndLinks(LinkBundle linkBundle, List<Link> links) {
        this.linkBundle = linkBundle;
        this.links = links;
    }
}
