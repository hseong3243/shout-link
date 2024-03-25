package com.seong.shoutlink.domain.link.service.event;

import com.seong.shoutlink.domain.common.Event;

public record CreateLinkEvent(Long linkId, String url) implements Event {

}
