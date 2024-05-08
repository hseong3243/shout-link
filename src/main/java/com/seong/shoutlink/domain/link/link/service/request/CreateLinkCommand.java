package com.seong.shoutlink.domain.link.link.service.request;

public record CreateLinkCommand(Long memberId, Long linkBundleId, String url, String description) {

}
