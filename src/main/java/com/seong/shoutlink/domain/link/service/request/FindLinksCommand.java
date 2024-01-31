package com.seong.shoutlink.domain.link.service.request;

public record FindLinksCommand(Long memberId, Long linkBundleId, int page, int size) {

}
