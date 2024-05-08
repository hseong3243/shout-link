package com.seong.shoutlink.domain.link.link.service.request;

public record FindLinksCommand(Long memberId, Long linkBundleId, int page, int size) {

}
