package com.seong.shoutlink.domain.hub.service.response;

import com.seong.shoutlink.domain.hub.Hub;
import java.util.List;

public record FindHubsResponse(List<FindHubResponse> hubs, long totalElements, boolean hasNext) {

    public static FindHubsResponse of(List<Hub> hubs, long totalElements, boolean hasNext) {
        List<FindHubResponse> content = hubs.stream()
            .map(FindHubResponse::from)
            .toList();
        return new FindHubsResponse(content, totalElements, hasNext);
    }
}
