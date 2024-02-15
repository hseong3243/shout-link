package com.seong.shoutlink.domain.hub.service.result;

import com.seong.shoutlink.domain.hub.Hub;
import java.util.List;

public record HubPaginationResult(List<Hub> hubs, long totalElements, boolean hasNext) {

}
