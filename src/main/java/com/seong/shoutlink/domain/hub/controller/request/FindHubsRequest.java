package com.seong.shoutlink.domain.hub.controller.request;

import java.util.Objects;

public record FindHubsRequest(Integer page, Integer size) {

    public FindHubsRequest(Integer page, Integer size) {
        this.page = Objects.isNull(page) ? 0 : page;
        this.size = Objects.isNull(size) ? 20 : size;
    }
}
