package com.seong.shoutlink.domain.link;

import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
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
