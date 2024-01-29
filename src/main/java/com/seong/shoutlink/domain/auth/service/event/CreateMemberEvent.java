package com.seong.shoutlink.domain.auth.service.event;

import com.seong.shoutlink.domain.common.Event;

public record CreateMemberEvent(Long memberId) implements Event {

}
