package com.seong.shoutlink.domain.hub.service.response;

import com.seong.shoutlink.domain.hub.Hub;

public record FindHubResponse(
    Long hubId,
    Long masterId,
    String name,
    String description,
    boolean isPrivate) {

    public static FindHubResponse from(Hub hub) {
        return new FindHubResponse(
            hub.getHubId(),
            hub.getMasterId(),
            hub.getName(),
            hub.getDescription(),
            hub.isPrivate());
    }
}
