package com.seong.shoutlink.domain.tag;

import com.seong.shoutlink.domain.hub.Hub;
import lombok.Getter;

@Getter
public class HubTag {

    private final Hub hub;
    private final Tag tag;

    public HubTag(Hub hub, Tag tag) {
        this.hub = hub;
        this.tag = tag;
    }

    public Long getHubId() {
        return hub.getHubId();
    }
}
