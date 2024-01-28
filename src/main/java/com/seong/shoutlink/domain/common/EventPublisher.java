package com.seong.shoutlink.domain.common;

public interface EventPublisher {

    void publishEvent(Event event);
}
