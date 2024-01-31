package com.seong.shoutlink.domain.link.service.request;

public record CreateLinkCommand(Long memberId, Long linkBundleId, String url, String description) {

}
