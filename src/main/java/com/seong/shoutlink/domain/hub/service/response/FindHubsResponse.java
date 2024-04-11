package com.seong.shoutlink.domain.hub.service.response;

import java.util.List;

public record FindHubsResponse(List<FindHubResponse> hubs, long totalElements, boolean hasNext) {

}
