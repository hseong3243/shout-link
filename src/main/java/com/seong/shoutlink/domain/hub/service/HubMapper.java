package com.seong.shoutlink.domain.hub.service;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.service.response.FindHubResponse;
import com.seong.shoutlink.domain.hub.service.result.HubTagResponse;
import com.seong.shoutlink.domain.hub.service.result.HubTagResult;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HubMapper {

    public static FindHubResponse findHubResponse(Hub hub, List<HubTagResult> tags) {
        List<HubTagResponse> hubTagResponses = tags.stream()
            .map(HubMapper::hubTagResponse)
            .toList();
        return new FindHubResponse(
            hub.getHubId(),
            hub.getMasterId(),
            hub.getName(),
            hub.getDescription(),
            hub.isPrivate(),
            hubTagResponses
        );
    }

    private static HubTagResponse hubTagResponse(HubTagResult result) {
        return new HubTagResponse(result.tagId(), result.name());
    }
}
