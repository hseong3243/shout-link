package com.seong.shoutlink.domain.link.service.request;

public record CreateHubLinkCommand(
    Long hubId,
    Long memberId,
    Long linkBundleId,
    String url,
    String description) {

}
