package com.seong.shoutlink.domain.hub.service.event;

import com.seong.shoutlink.domain.common.Event;

public record CreateHubEvent(Long hubId, Long memberId) implements Event {

}
