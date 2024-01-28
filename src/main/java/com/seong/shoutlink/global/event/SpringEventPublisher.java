package com.seong.shoutlink.global.event;

import com.seong.shoutlink.domain.common.Event;
import com.seong.shoutlink.domain.common.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public class SpringEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishEvent(Event event) {
        applicationEventPublisher.publishEvent(event);
    }
}
