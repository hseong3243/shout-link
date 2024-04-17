package com.seong.shoutlink.domain.member.service.event;

import com.seong.shoutlink.domain.common.Event;

public record CreateMemberEvent(Long memberId) implements Event {

}
