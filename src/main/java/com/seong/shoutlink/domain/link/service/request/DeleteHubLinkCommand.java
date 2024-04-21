package com.seong.shoutlink.domain.link.service.request;

public record DeleteHubLinkCommand(Long linkId, Long memberId, Long hubId) {

}
