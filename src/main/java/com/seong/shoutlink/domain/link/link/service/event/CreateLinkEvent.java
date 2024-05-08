package com.seong.shoutlink.domain.link.link.service.event;

import com.seong.shoutlink.domain.common.Event;

public abstract class CreateLinkEvent implements Event {

    private final Long linkId;
    private final String url;

    protected CreateLinkEvent(Long linkId, String url) {
        this.linkId = linkId;
        this.url = url;
    }

    public Long linkId() {
        return linkId;
    }

    public String url() {
        return url;
    }
}
