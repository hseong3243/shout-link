package com.seong.shoutlink.domain.hub.service.request;

public record CreateHubCommand(Long memberId, String name, String description) {

}
