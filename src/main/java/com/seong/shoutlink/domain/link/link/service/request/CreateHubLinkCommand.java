package com.seong.shoutlink.domain.link.link.service.request;

import java.time.LocalDateTime;

public record CreateHubLinkCommand(
    Long hubId,
    Long memberId,
    Long linkBundleId,
    String url,
    String description,
    LocalDateTime expiredAt) {

}
