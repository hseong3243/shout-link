package com.seong.shoutlink.domain.linkbundle;

import com.seong.shoutlink.domain.hub.Hub;
import lombok.Getter;

@Getter
public class HubLinkBundle {

    private final Hub hub;
    private final LinkBundle linkBundle;

    public HubLinkBundle(Hub hub, LinkBundle linkBundle) {
        this.hub = hub;
        this.linkBundle = linkBundle;
    }
}
