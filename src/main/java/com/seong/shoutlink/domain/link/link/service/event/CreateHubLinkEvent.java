package com.seong.shoutlink.domain.link.link.service.event;

import java.util.Objects;

public class CreateHubLinkEvent extends CreateLinkEvent {

    private final Long hubId;

    public CreateHubLinkEvent(Long linkId, String url, Long hubId) {
        super(linkId, url);
        this.hubId = hubId;
    }

    public Long hubId() {
        return hubId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        CreateHubLinkEvent that = (CreateHubLinkEvent) o;
        return Objects.equals(hubId, that.hubId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hubId);
    }

    @Override
    public String toString() {
        return "CreateHubLinkEvent{" +
            "hubId=" + hubId +
            '}';
    }
}
