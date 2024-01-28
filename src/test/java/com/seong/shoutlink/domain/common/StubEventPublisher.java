package com.seong.shoutlink.domain.common;

public class StubEventPublisher implements EventPublisher {

    private int publishEventCount = 0;

    @Override
    public void publishEvent(Event event) {
        publishEventCount++;
    }

    public int getPublishEventCount() {
        return publishEventCount;
    }
}