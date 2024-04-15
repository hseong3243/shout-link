package com.seong.shoutlink.domain.hub.service.request;

public record FindMyHubsCommand(int page, int size, Long memberId) {

}
