package com.seong.shoutlink.domain.hub.service.response;

import com.seong.shoutlink.domain.hub.service.result.HubTagResponse;
import java.util.List;

public record FindHubResponse(
    Long hubId,
    Long masterId,
    String name,
    String description,
    boolean isPrivate,
    List<HubTagResponse> tags) {

}
