package com.seong.shoutlink.domain.link.service.event;

import com.seong.shoutlink.domain.common.Event;

public record CreateLinkEvent(String url) implements Event {

}
